package com.example.userschedule.exception;

import org.springframework.http.HttpStatus;

public class PasswordDoesNotMatch extends ServiceException{
    public PasswordDoesNotMatch(String message){
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
