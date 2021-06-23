package com.example.exchangechanginggifservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.example.exchangechanginggifservice")
public class ExchangeChangingGifServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeChangingGifServiceApplication.class, args);
    }

}
