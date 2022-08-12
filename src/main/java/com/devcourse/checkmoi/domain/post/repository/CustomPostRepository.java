package com.devcourse.checkmoi.domain.post.repository;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.Posts;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Posts findAllByCondition(Long userId,
        Search search,
        Pageable pageable);

}
