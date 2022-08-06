package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    private final PostConverter postConverter;

    // TODO: validation
    // TODO: pageable & dynamic search
    @Override
    public List<PostInfo> findAllPosts(Long userId, Search request) {
        return postRepository.findAll().stream()
            .map(postConverter::postToInfo).toList();
    }

    @Override
    public PostInfo findByPostId(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        return postConverter.postToInfo(post);
    }

}
