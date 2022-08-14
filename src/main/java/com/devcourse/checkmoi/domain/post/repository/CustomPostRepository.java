package com.devcourse.checkmoi.domain.post.repository;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Page<PostResponse.PostInfo> findAllByCondition(Long userId,
        Search search,
        Pageable pageable);

}
