package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;

public interface PostCommandService {

    Long createPost(Long userId, Create request);

    void editPost(Long userId, Long postId, Edit request);

    void deletePost(Long userId, Long postId);
}
