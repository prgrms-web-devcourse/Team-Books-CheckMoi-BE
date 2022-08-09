package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.EntityNotFoundException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class StudyNotFoundException extends EntityNotFoundException {

    public StudyNotFoundException() {
        super(ErrorMessage.STUDY_NOT_FOUND);
    }

}
