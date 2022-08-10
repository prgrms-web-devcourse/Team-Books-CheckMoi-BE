package com.devcourse.checkmoi.domain.user.exception;

import com.devcourse.checkmoi.global.exception.InvalidValueException;

public class UserInvalidValueException extends InvalidValueException {

    public UserInvalidValueException(String message) {
        super(message);
    }
    
}
