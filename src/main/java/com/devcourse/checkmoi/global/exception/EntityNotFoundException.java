package com.devcourse.checkmoi.global.exception;

import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super(message, ErrorMessage.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(String message, ErrorMessage errorMessage) {
        super(message, errorMessage);
    }

    public EntityNotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
