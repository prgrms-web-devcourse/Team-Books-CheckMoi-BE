package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.Posts;
import com.devcourse.checkmoi.global.model.SimplePage;

public interface PostQueryService {

    Posts findAllByCondition(Long userId, Search request, SimplePage page);

    PostInfo findByPostId(Long userId, Long postId);
}
