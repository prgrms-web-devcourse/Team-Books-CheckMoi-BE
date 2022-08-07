package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;

public interface CommentCommandService {

    void deleteById(Long userId, Long commentId);

    Long createComment(Long studyId, Long postId, Long userId, Create request);
}
