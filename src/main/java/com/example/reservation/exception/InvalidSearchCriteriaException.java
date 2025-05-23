package com.example.reservation.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSearchCriteriaException extends IllegalArgumentException {
    public InvalidSearchCriteriaException(String message) {
        super(message);
    }
}

