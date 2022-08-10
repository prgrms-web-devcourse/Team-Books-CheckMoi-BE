package com.devcourse.checkmoi.global.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class DuplicateException extends BusinessException {

    public DuplicateException(String message,
        ErrorMessage errorMessage) {
        super(message, errorMessage);
    }

    public DuplicateException(ErrorMessage errorCode) {
        super(errorCode);
    }
}
