package com.devcourse.checkmoi.domain.post.exception;

import com.devcourse.checkmoi.global.exception.EntityNotFoundException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException() {
        super(ErrorMessage.POST_NOT_FOUND);
    }

}
