package com.devcourse.checkmoi.domain.post.repository;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import java.util.List;

public interface CustomPostRepository {

    List<PostInfo> findAllByCondition(Long userId, Search request);
}
