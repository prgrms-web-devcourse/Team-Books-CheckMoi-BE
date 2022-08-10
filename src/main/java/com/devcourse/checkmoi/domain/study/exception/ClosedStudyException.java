package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.InvalidValueException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class ClosedStudyException extends InvalidValueException {

    public ClosedStudyException(String message) {
        super(message, ErrorMessage.CLOSED_STUDY);
    }
}
