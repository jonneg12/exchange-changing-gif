package com.example.exchangechanginggifservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private String code;
    private String message;
}
