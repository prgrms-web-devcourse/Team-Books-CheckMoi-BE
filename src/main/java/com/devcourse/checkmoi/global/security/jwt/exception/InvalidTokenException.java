package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class InvalidTokenException extends TokenException {

    public InvalidTokenException() {
        super(ErrorMessage.INVALID_TOKEN);
    }

}
