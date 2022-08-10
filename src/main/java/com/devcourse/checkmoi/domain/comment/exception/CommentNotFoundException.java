package com.devcourse.checkmoi.domain.comment.exception;

import com.devcourse.checkmoi.global.exception.EntityNotFoundException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException() {
        super(ErrorMessage.COMMENT_NOT_FOUND);
    }

}
