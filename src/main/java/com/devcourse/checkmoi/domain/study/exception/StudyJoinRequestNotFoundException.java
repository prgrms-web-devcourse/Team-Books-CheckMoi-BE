package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.EntityNotFoundException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class StudyJoinRequestNotFoundException extends EntityNotFoundException {

    public StudyJoinRequestNotFoundException(
        ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
