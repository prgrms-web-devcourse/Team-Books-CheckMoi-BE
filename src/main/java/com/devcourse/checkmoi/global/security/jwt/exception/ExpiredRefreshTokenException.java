package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class ExpiredRefreshTokenException extends TokenException {

    public ExpiredRefreshTokenException() {
        super(ErrorMessage.EXPIRED_REFRESH_TOKEN);
    }

}
