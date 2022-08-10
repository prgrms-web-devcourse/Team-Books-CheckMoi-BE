package com.devcourse.checkmoi.domain.file.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class NotAllowedFileException extends BusinessException {

    public NotAllowedFileException() {
        super(ErrorMessage.NOT_ALLOWED_FILE);
    }
}
