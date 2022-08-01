package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.DuplicateException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class DuplicateStudyJoinRequestException extends DuplicateException {

    public DuplicateStudyJoinRequestException(
        ErrorMessage errorCode) {
        super(errorCode);
    }
}
