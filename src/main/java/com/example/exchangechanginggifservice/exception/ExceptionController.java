package com.example.exchangechanginggifservice.exception;

import com.example.exchangechanginggifservice.model.ErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExchangeException.class)
    ResponseEntity<ErrorModel> handleExchangeException(ExchangeException exception){
        log.error("Exchange exception error: {}", exception.getMessage());
        return new ResponseEntity<>(new ErrorModel(exception.getCode(), exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GifException.class)
    ResponseEntity<ErrorModel> handleGifException(GifException exception){
        log.error("Exchange exception error: {}", exception.getMessage());
        return new ResponseEntity<>(new ErrorModel(exception.getCode(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handle(Exception ex) {
        log.error("Internal Server Error");
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorModel("s01", "internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
