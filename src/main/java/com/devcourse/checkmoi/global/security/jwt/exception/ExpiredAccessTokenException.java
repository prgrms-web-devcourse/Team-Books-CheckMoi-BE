package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class ExpiredAccessTokenException extends TokenException {

    public ExpiredAccessTokenException() {
        super(ErrorMessage.EXPIRED_ACCESS_TOKEN);
    }

}
