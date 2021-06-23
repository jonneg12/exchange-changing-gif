package com.example.exchangechanginggifservice.feign;


import com.example.exchangechanginggifservice.model.ExchangeRatesModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.openexchanherates.api.name}", url = "${feign.openexchanherates.api.url}")
public interface OpenExchangeRatesApi {

    @GetMapping("/api/latest.json")
    ExchangeRatesModel getTodayExchangeRates(
            @RequestParam(value = "app_id") String appId,
            @RequestParam(value = "base") String base);

    @GetMapping("/api/historical/{date}.json")
    ExchangeRatesModel getExchangeCurrenciesByDate(
            @PathVariable(name = "date") String date,
            @RequestParam(value = "app_id") String appId,
            @RequestParam(value = "base") String base);
}
