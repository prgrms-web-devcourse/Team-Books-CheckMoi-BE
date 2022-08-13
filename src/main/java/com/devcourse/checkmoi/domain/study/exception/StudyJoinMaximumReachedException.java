package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.InvalidStatusException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class StudyJoinMaximumReachedException extends InvalidStatusException {

    public StudyJoinMaximumReachedException() {
        super(ErrorMessage.STUDY_JOIN_MAXIMUM_REACHED);
    }
}
