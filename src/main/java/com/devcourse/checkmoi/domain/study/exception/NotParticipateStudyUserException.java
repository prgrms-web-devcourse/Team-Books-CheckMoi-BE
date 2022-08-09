package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.ErrorMessage;
import com.devcourse.checkmoi.global.exception.InvalidStatusException;

public class NotParticipateStudyUserException extends InvalidStatusException {

    public NotParticipateStudyUserException() {
        super(ErrorMessage.NOT_PARTICIPATE_STUDY_USER);
    }
}
