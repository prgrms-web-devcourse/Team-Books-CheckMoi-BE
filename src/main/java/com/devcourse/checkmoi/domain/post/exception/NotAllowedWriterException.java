package com.devcourse.checkmoi.domain.post.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class NotAllowedWriterException extends BusinessException {

    public NotAllowedWriterException(String message) {
        super(message, ErrorMessage.NOT_ALLOWED_WRITER);
    }
}

