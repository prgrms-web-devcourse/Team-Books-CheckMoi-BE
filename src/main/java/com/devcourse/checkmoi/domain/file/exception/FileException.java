package com.devcourse.checkmoi.domain.file.exception;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.ErrorMessage;

public class FileException extends BusinessException {

    public FileException(Throwable cause) {
        super(cause, ErrorMessage.INTERNAL_SERVER_ERROR);
    }
}
