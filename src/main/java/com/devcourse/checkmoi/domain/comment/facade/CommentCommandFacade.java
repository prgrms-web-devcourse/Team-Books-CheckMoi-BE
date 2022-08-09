package com.devcourse.checkmoi.domain.comment.facade;


import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;

public interface CommentCommandFacade {
    Long createComment(Long postId, Long userId, Create request);
}
