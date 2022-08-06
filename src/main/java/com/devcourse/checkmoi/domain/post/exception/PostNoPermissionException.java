package com.devcourse.checkmoi.domain.post.exception;

import com.devcourse.checkmoi.global.exception.ErrorMessage;
import com.devcourse.checkmoi.global.exception.InvalidValueException;

public class PostNoPermissionException extends InvalidValueException {

    private static final String MESSAGE = "게시글 접근 권한이 없습니다.";

    public PostNoPermissionException() {
        super(MESSAGE, ErrorMessage.ACCESS_DENIED);
    }
}
