package com.example.exchangechanginggifservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OutOfSuchCurrencyException extends RuntimeException {
}
