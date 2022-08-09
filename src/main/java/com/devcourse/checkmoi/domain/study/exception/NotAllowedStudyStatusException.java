package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;
import com.devcourse.checkmoi.global.exception.InvalidStatusException;

public class NotAllowedStudyStatusException extends InvalidStatusException {

    public NotAllowedStudyStatusException() {
        super(ErrorMessage.NOT_ALLOWED_STUDY_STATUS);
    }
}