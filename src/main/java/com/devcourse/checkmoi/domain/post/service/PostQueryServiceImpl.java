package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public List<PostInfo> findAllPosts(Long id, Search request) {
        return null;
    }

    @Override
    public PostInfo findByPostId(Long id, Long postId) {
        return null;
    }
}
