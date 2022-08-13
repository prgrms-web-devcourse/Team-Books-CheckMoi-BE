package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class TokenException extends BusinessException {

    public TokenException(ErrorMessage errorCode) {
        super(errorCode);
    }

}
