package com.devcourse.checkmoi.domain.comment.service.validator;

import com.devcourse.checkmoi.domain.comment.model.Comment;

public interface CommentValidator {

    void editComment(Comment comment, Long userId);
}
