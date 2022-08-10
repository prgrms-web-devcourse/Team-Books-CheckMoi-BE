package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;
import com.devcourse.checkmoi.global.exception.InvalidValueException;

public class NotStudyOwnerException extends InvalidValueException {

    public NotStudyOwnerException(String value, ErrorMessage errorMessage) {
        super(value, errorMessage);
    }
}
