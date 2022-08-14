package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.InvalidStatusException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class NotRecruitingStudyException extends InvalidStatusException {

    public NotRecruitingStudyException() {
        super(ErrorMessage.NOT_RECRUITING_STUDY);
    }
}
