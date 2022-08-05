package com.devcourse.checkmoi.global.exception;

public class BusinessException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public BusinessException(String message, ErrorMessage errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    public BusinessException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BusinessException(Throwable cause, ErrorMessage errorMessage) {
        super(cause);
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return this.errorMessage;
    }
}
