package com.example.exchangechanginggifservice.controller;

import com.example.exchangechanginggifservice.model.ExchangeRateChangeStatus;
import com.example.exchangechanginggifservice.model.GifModel;
import com.example.exchangechanginggifservice.service.ExchangeRatesService;
import com.example.exchangechanginggifservice.service.GifService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class MainController {

    ExchangeRatesService exchangeService;
    GifService gifService;

    @Autowired
    public MainController(ExchangeRatesService exchangeService, GifService gifService) {
        this.exchangeService = exchangeService;
        this.gifService = gifService;
    }

    @GetMapping("/{currency}")
    public ExchangeRateChangeStatus getDailyChange(@PathVariable(value = "currency") String currency) {
        final ExchangeRateChangeStatus dailyChange = exchangeService.getDailyChange(currency.toUpperCase());
        log.info("Daily change for currency {} is {}", currency, dailyChange);
        return dailyChange;
    }

    @GetMapping("/gif/{currency}")
    public GifModel getDailyChangeGif(@PathVariable(value = "currency") String currency) {
        final ExchangeRateChangeStatus dailyChange = exchangeService.getDailyChange(currency);
        return gifService.getGif(dailyChange.getQ());
    }
}
