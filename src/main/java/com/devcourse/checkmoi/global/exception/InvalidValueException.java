package com.devcourse.checkmoi.global.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String value) {
        super(value, ErrorMessage.INVALID_INPUT);
    }

    public InvalidValueException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidValueException(String value, ErrorMessage errorMessage) {
        super(value, errorMessage);
    }
}
