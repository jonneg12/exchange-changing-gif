package com.example.exchangechanginggifservice.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum ExchangeRateChangeStatus {
    INCREASED("increased", "rich"),
    DECREASED("decreased", "broke"),
    STAY("did not changed", "rich");

    private final String describe;
    private final String q;

    ExchangeRateChangeStatus(String describe, String q) {
        this.describe = describe;
        this.q = q;
    }
}
