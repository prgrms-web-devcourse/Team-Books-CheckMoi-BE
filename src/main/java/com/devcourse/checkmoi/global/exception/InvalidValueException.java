package com.devcourse.checkmoi.global.exception;

import org.springframework.http.HttpStatus;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
