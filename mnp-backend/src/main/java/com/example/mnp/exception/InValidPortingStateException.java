package com.example.mnp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InValidPortingStateException extends RuntimeException{
    public InValidPortingStateException(String message){
        super(message);
    }
}
