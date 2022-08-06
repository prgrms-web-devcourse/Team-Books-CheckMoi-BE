package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import java.util.List;

public interface PostQueryService {

    List<PostInfo> findAllPosts(Long userId, Search request);

    PostInfo findByPostId(Long userId, Long postId);
}
