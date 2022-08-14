package com.devcourse.checkmoi.global.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class InvalidAuthorizeException extends BusinessException {

    public InvalidAuthorizeException(String message,
        ErrorMessage errorMessage) {
        super(message, errorMessage);
    }

    public InvalidAuthorizeException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidAuthorizeException(Throwable cause,
        ErrorMessage errorMessage) {
        super(cause, errorMessage);
    }
}
