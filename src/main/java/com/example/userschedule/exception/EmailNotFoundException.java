package com.example.userschedule.exception;

import org.springframework.http.HttpStatus;

public class EmailNotFoundException extends ServiceException{
    public EmailNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
