package com.devcourse.checkmoi.domain.post.service.validator;

public interface PostServiceValidator {

    void validatePostOwner(Long userId, Long postId);
    
}
