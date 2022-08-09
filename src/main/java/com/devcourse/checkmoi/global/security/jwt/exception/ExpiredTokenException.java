package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class ExpiredTokenException extends TokenException {

    public ExpiredTokenException() {
        super(ErrorMessage.EXPIRED_TOKEN);
    }

}
