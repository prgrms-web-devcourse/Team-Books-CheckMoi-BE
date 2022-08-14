package com.devcourse.checkmoi.domain.comment.service.validator;

public interface CommentValidator {

    void commentPermission(Long userId, Long... compareId);
}
