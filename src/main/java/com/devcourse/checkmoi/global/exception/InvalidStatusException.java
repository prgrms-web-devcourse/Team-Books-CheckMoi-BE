package com.devcourse.checkmoi.global.exception;

public class InvalidStatusException extends BusinessException {

    public InvalidStatusException(String message,
        ErrorMessage errorMessage) {
        super(message, errorMessage);
    }

    public InvalidStatusException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidStatusException(Throwable cause,
        ErrorMessage errorMessage) {
        super(cause, errorMessage);
    }
}
