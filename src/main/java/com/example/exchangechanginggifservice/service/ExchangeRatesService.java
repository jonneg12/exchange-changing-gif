package com.example.exchangechanginggifservice.service;

import com.example.exchangechanginggifservice.model.ExchangeRateChangeStatus;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRatesService {

    Map<String, BigDecimal> getTodayExchangeRates();

    Map<String, BigDecimal> getYesterdayExchangeRates();

    ExchangeRateChangeStatus getDailyChange(String currency);
}
