package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.post.service.validator.PostServiceValidator;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    // 공통 : 글을 작성한 본인만 UD 권한이 있다

    private final PostRepository postRepository;

    private final StudyMemberRepository studyMemberRepository;

    private final PostConverter postConverter;

    private final PostServiceValidator postValidator;

    @Override
    public Long createPost(Long userId, Create request) {
        StudyMember studyMember = studyMemberRepository.findByUser(userId)
            .orElseThrow(UserNotFoundException::new);

        PostCategory.valueOf(request.category())
            .checkAllowedWriter(studyMember);

        return postRepository.save(postConverter.createToPost(request, userId)).getId();
    }

    @Override
    public void editPost(Long userId, Long postId, Edit request) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        postValidator.validatePostOwner(userId, post.getWriter().getId());

        post.editTitle(request.title());
        post.editContent(request.content());
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        postValidator.validatePostOwner(userId, post.getWriter().getId());
        postRepository.deleteById(postId);
    }
}
