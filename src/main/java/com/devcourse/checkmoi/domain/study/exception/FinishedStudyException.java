package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.InvalidStatusException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class FinishedStudyException extends InvalidStatusException {

    public FinishedStudyException() {
        super(ErrorMessage.FINISHED_STUDY);
    }
}
