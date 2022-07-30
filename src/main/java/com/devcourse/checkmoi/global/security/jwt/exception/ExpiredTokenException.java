package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class ExpiredTokenException extends TokenException {

    public ExpiredTokenException() {
        super(ErrorMessage.EXPIRED_TOKEN);
    }

}
