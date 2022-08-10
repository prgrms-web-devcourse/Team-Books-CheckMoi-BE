package com.devcourse.checkmoi.domain.user.exception;


import com.devcourse.checkmoi.global.exception.EntityNotFoundException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super(ErrorMessage.USER_NOT_FOUND);
    }

}
