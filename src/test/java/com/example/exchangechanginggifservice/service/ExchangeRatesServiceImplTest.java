package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.exception.ExchangeException;
import com.example.exchangechanginggifservice.exception.GifException;
import com.example.exchangechanginggifservice.feign.OpenExchangeRatesApi;
import com.example.exchangechanginggifservice.model.ExchangeRateChangeStatus;
import com.example.exchangechanginggifservice.model.ExchangeRatesModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@Slf4j
@SpringBootTest
class ExchangeRatesServiceImplTestApi {

    @MockBean
    private OpenExchangeRatesApi api;

    @Autowired
    private ExchangeRatesServiceImpl service;

    @Value("${service.exchange.openexchangerates.api.id}")
    private String openExchangeRatesApiId;

    @Value(("${service.exchange.base-currency}"))
    private String baseCurrency;

    @Value("${service.exchange.tracked-currency}")
    private String trackedCurrency;

    @Value("${service.exchange.calculation-accuracy}")
    private String accuracyLine;

    @Test
    void getTodayExchangeRates() {
        Map<String, BigDecimal> mockedRates = new HashMap<>();
        mockedRates.put("RUB", new BigDecimal(20.5));
        mockedRates.put("EUR", new BigDecimal(0.5));
        mockedRates.put("GBP", new BigDecimal(0.4));

        Mockito.when(api.getTodayExchangeRates(openExchangeRatesApiId, baseCurrency))
                .thenReturn(new ExchangeRatesModel(baseCurrency, mockedRates));
        final Map<String, BigDecimal> todayExchangeRates = service.getTodayExchangeRates();
        Assert.isTrue(todayExchangeRates.equals(mockedRates), "should be equals");
    }

    @Test
    void getYesterdayExchangeRates() {
            Map<String, BigDecimal> mockedRates = new HashMap<>();
            mockedRates.put("RUB", new BigDecimal(20.5));
            mockedRates.put("EUR", new BigDecimal(0.5));
            mockedRates.put("GBP", new BigDecimal(0.4));

            Mockito.when(api.getExchangeCurrenciesByDate(anyString(), anyString(), anyString()))
                    .thenReturn(new ExchangeRatesModel(baseCurrency, mockedRates));
        final Map<String, BigDecimal> yesterdayExchangeRates = service.getYesterdayExchangeRates();
        Assert.isTrue(yesterdayExchangeRates.equals(mockedRates), "should be equals");
    }

    @Test
    void getDailyChangeTest() {
        Map<String, BigDecimal> mockedYesterdayRates = new HashMap<>();
        mockedYesterdayRates.put("RUB", new BigDecimal(72.897));
        mockedYesterdayRates.put("USD", new BigDecimal(1));
        mockedYesterdayRates.put("EUR", new BigDecimal(0.83895));
        mockedYesterdayRates.put("GBP", new BigDecimal(0.716874));
        mockedYesterdayRates.put("ALL", new BigDecimal(72.897));
        Mockito.when(api.getExchangeCurrenciesByDate(anyString(), anyString(), anyString()))
                .thenReturn(new ExchangeRatesModel(baseCurrency, mockedYesterdayRates));

        Map<String, BigDecimal> mockedTodayRates = new HashMap<>();
        mockedTodayRates.put("RUB", new BigDecimal(72.8334));
        mockedTodayRates.put("USD", new BigDecimal(1));
        mockedTodayRates.put("EUR", new BigDecimal(0.837774));
        mockedTodayRates.put("GBP", new BigDecimal(0.717802));
        mockedTodayRates.put("ALL", new BigDecimal(72.8334));

        Mockito.when(api.getTodayExchangeRates(openExchangeRatesApiId, baseCurrency))
                .thenReturn(new ExchangeRatesModel(baseCurrency, mockedTodayRates));

        final ExchangeRateChangeStatus allRate = service.getDailyChange("ALL");
        final ExchangeRateChangeStatus eurRate = service.getDailyChange("EUR");
        final ExchangeRateChangeStatus gbpRate = service.getDailyChange("GBP");

        Assert.isTrue(allRate.equals(ExchangeRateChangeStatus.STAY), "currency shouldn't be changed");
        Assert.isTrue(gbpRate.equals(ExchangeRateChangeStatus.DECREASED), "currency should be decreased");
        Assert.isTrue(eurRate.equals(ExchangeRateChangeStatus.INCREASED), "currency should be increased");
    }

    @Test
    void getDailyChangeTestThrowExchangeException() {
        Map<String, BigDecimal> mockedYesterdayRates = new HashMap<>();
        mockedYesterdayRates.put("RUB", new BigDecimal(72.897));
        mockedYesterdayRates.put("USD", new BigDecimal(1));
        mockedYesterdayRates.put("EUR", new BigDecimal(0.83895));
        mockedYesterdayRates.put("GBP", new BigDecimal(0.716874));
        mockedYesterdayRates.put("ALL", new BigDecimal(72.897));
        Mockito.when(api.getExchangeCurrenciesByDate(anyString(), anyString(), anyString()))
                .thenReturn(new ExchangeRatesModel(baseCurrency, mockedYesterdayRates));

        Map<String, BigDecimal> mockedTodayRates = new HashMap<>();
        mockedTodayRates.put("RUB", new BigDecimal(72.8334));
        mockedTodayRates.put("USD", new BigDecimal(1));
        mockedTodayRates.put("EUR", new BigDecimal(0.837774));
        mockedTodayRates.put("GBP", new BigDecimal(0.717802));
        mockedTodayRates.put("ALL", new BigDecimal(72.8334));

        Mockito.when(api.getTodayExchangeRates(openExchangeRatesApiId, baseCurrency))
                .thenReturn(new ExchangeRatesModel(baseCurrency, mockedTodayRates));

        Assertions.assertThrows(ExchangeException.class,
                () -> service.getDailyChange("asd"));
    }
}