package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class TokenException extends BusinessException {

    public TokenException(String message, ErrorMessage errorMessage) {
        super(message, errorMessage);
    }

    public TokenException(ErrorMessage errorCode) {
        super(errorCode);
    }

}
