package com.example.exchangechanginggifservice.feign;

import com.example.exchangechanginggifservice.model.GiphyModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.giphy.api.name}", url = "${feign.giphy.api.url}")

public interface GiphyApi {
    @GetMapping("/v1/gifs/search")
    GiphyModel getGifs(@RequestParam(value = "api_key") String apiKey,
                       @RequestParam(value = "q") String q);
}
