package com.devcourse.checkmoi.domain.study.exception;

import com.devcourse.checkmoi.global.exception.InvalidStatusException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class NotJoinedMemberException extends InvalidStatusException {

    public NotJoinedMemberException() {
        super(ErrorMessage.NOT_JOINED_USER);
    }

    public NotJoinedMemberException(String message) {
        super(message, ErrorMessage.NOT_JOINED_USER);
    }
}
