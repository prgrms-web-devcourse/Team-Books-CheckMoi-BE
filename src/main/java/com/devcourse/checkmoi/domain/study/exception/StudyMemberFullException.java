package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.InvalidStatusException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class StudyMemberFullException extends InvalidStatusException {

    public StudyMemberFullException() {
        super(ErrorMessage.STUDY_IS_FULL);
    }
}
