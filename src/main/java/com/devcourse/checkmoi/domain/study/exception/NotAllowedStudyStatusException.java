package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class NotAllowedStudyStatusException extends BusinessException {

    public NotAllowedStudyStatusException() {
        super(ErrorMessage.NOT_ALLOWED_STUDY_STATUS);
    }
}
