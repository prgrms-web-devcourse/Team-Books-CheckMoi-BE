package com.devcourse.checkmoi.domain.comment.service;

public interface CommentCommandService {

    void deleteById(Long userId, Long commentId);
}
