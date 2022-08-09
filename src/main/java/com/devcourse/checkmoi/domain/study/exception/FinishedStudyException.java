package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.ErrorMessage;
import com.devcourse.checkmoi.global.exception.InvalidStatusException;

public class FinishedStudyException extends InvalidStatusException {
    public FinishedStudyException() {
        super(ErrorMessage.FINISHED_STUDY);
    }
}
