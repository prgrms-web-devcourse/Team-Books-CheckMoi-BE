package com.devcourse.checkmoi.domain.user.exception;

import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.ACCESS_DENIED;
import com.devcourse.checkmoi.global.exception.InvalidValueException;

public class UserNoPermissionException extends InvalidValueException {

    public UserNoPermissionException(String message) {
        super(message, ACCESS_DENIED);
    }
}
