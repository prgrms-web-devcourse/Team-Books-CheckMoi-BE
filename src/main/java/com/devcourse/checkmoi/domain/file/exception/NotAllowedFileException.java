package com.devcourse.checkmoi.domain.file.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class NotAllowedFileException extends BusinessException {

    public NotAllowedFileException() {
        super(ErrorMessage.NOT_ALLOWED_FILE);
    }
}
