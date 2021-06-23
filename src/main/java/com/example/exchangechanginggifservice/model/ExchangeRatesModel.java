package com.example.exchangechanginggifservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesModel {

    private String base;
    private Map<String, BigDecimal> rates;
}
