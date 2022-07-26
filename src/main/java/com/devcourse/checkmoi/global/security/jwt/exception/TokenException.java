package com.devcourse.checkmoi.global.security.jwt.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TokenException extends BusinessException {

    public TokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
