package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;

    @Override
    public Long createPost(Long id, Create request) {
        return null;
    }

    @Override
    public void editPost(Long id, Edit request) {

    }

    @Override
    public void deletePost(Long id, Long postId) {

    }
}
