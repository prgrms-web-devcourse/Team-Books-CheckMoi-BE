package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;

public interface PostCommandService {

    Long createPost(Long id, Create request);

    void editPost(Long id, Edit request);

    void deletePost(Long id, Long postId);
}
