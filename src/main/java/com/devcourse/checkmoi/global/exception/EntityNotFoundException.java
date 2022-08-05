package com.devcourse.checkmoi.global.exception;

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
