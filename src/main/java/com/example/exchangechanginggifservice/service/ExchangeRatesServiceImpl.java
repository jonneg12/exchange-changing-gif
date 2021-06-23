package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.exception.OutOfSuchCurrencyException;
import com.example.exchangechanginggifservice.exception.ExchangeException;
import com.example.exchangechanginggifservice.feign.OpenExchangeRatesApi;
import com.example.exchangechanginggifservice.model.ExchangeRateChangeStatus;
import com.example.exchangechanginggifservice.model.ExchangeRatesModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesServiceImpl implements ExchangeRatesService {
    private static final int DEFAULT_ACCURACY = 6;
    private Map<String, BigDecimal> rates = new ConcurrentHashMap<>();

    private OpenExchangeRatesApi api;

    @Value("${service.exchange.openexchangerates.api.id}")
    private String openExchangeRatesApiId;

    @Value(("${service.exchange.base-currency}"))
    private String baseCurrency;

    @Value("${service.exchange.tracked-currency}")
    private String trackedCurrency;

    @Value("${service.exchange.calculation-accuracy}")
    private String accuracyLine;

    @Autowired
    public ExchangeRatesServiceImpl(OpenExchangeRatesApi api) {
        log.info("Exchange service constructor");
        this.api = api;
    }

    @Override
    public Map<String, BigDecimal> getTodayExchangeRates() {
        log.info("Today exchange service get rates");
        try {
            ExchangeRatesModel exchangeRates = api.getTodayExchangeRates(openExchangeRatesApiId, baseCurrency);

            log.info("Got exchange rates: {}", exchangeRates);
            return exchangeRates.getRates();
        } catch (Exception e) {
            log.error("Today Exchange Rates exception {}", e.getMessage());
            throw new ExchangeException("e01", "Couldn't get today exchange rates");
        }
    }

    @Override
    public Map<String, BigDecimal> getYesterdayExchangeRates() {
        log.info("Yesterday exchange service get rates");
        String yesterdayDate = getYesterdayDate();

        try {
            ExchangeRatesModel exchangeRates = api.getExchangeCurrenciesByDate(yesterdayDate, openExchangeRatesApiId, baseCurrency);
            return exchangeRates.getRates();
        } catch (Exception e) {
            log.error("Yesterday Exchange Rates exception {}", e.getMessage());
            throw new ExchangeException("e02", "Couldn't get yesterday exchange rates");
        }
    }

    @Override
    public ExchangeRateChangeStatus getDailyChange(String currency) {
        currency = currency.toUpperCase();
        final Map<String, BigDecimal> todayRates = getTodayExchangeRates();
        final Map<String, BigDecimal> yesterdayRates = getYesterdayExchangeRates();

        if (!checkCurrency(currency, todayRates, yesterdayRates)){
            throw new ExchangeException("e03", "Couldn't find currency " + currency + " in rates");
        }

        final BigDecimal todayRateTrackedCurrency = todayRates.get(trackedCurrency);
        log.info("Today rate of {}/{} is {}", baseCurrency, trackedCurrency, todayRateTrackedCurrency);

        final BigDecimal yesterdayRateTrackedCurrency = yesterdayRates.get(trackedCurrency);
        log.info("Yesterday rate of {}/{} was {}", baseCurrency, trackedCurrency, yesterdayRateTrackedCurrency);

        final BigDecimal todayRateInputCurrency = todayRates.get(currency);
        log.info("Today rate of {}/{} is {}", baseCurrency, currency, todayRateInputCurrency);

        final BigDecimal yesterdayRateInputCurrency = yesterdayRates.get(currency);
        log.info("Yesterday rate of {}/{} was {}", baseCurrency, currency, yesterdayRateInputCurrency);

        int accuracy = getAccuracy(accuracyLine);

        final BigDecimal todayExchangeCourse = todayRateTrackedCurrency.divide(
                todayRateInputCurrency, accuracy, RoundingMode.HALF_UP);
        final BigDecimal yesterdayExchangeCourse = yesterdayRateTrackedCurrency.divide(
                yesterdayRateInputCurrency, accuracy, RoundingMode.HALF_UP);

        log.info("{}/{}: {} -> {}", currency, trackedCurrency, yesterdayExchangeCourse, todayExchangeCourse);

        ExchangeRateChangeStatus rateChanging = null;
        switch (todayExchangeCourse.compareTo(yesterdayExchangeCourse)) {
            case (1):
                rateChanging = ExchangeRateChangeStatus.INCREASED;
                break;
            case (-1):
                rateChanging = ExchangeRateChangeStatus.DECREASED;
                break;
            case (0):
                rateChanging = ExchangeRateChangeStatus.STAY;
                break;
        }

        log.info("The exchange rate of {} against {} is {} ", currency, trackedCurrency, rateChanging.getDescribe());
        return rateChanging;
    }

    private boolean checkCurrency(String currency, Map<String, BigDecimal> todayRates, Map<String, BigDecimal> yesterdayRates) {
        return todayRates.containsKey(currency) && yesterdayRates.containsKey(currency);
    }


    private int getAccuracy(String accuracyLine) {
        int accuracy = DEFAULT_ACCURACY;
        try {
            accuracy = Integer.parseInt(accuracyLine);
        } catch (NumberFormatException exception) {
            log.error("Cannot parse {} to int. Set to ({})", accuracyLine, accuracy);
        }
        return accuracy;
    }

    private String getYesterdayDate() {
        final String yesterdayDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("Yesterday date: {}", yesterdayDate);
        return yesterdayDate;
    }
}
