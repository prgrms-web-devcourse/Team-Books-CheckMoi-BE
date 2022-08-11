package com.devcourse.checkmoi.domain.comment.exception;

import com.devcourse.checkmoi.global.exception.InvalidAuthorizeException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class CommentNoPermissionException extends InvalidAuthorizeException {

    public CommentNoPermissionException() {
        super(ErrorMessage.COMMENT_NO_PERMISSION);
    }
}
