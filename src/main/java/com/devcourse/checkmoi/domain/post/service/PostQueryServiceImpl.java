package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.post.service.validator.PostServiceValidator;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
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

    private final PostServiceValidator validator;

    private final StudyMemberRepository memberRepository;

    // TODO: validation
    // TODO: pageable
    @Override
    public List<PostInfo> findAllByCondition(Long userId, Search request) {
        return postRepository.findAllByCondition(userId, request);
    }

    @Override
    public PostInfo findByPostId(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(PostNotFoundException::new);
        Study study = post.getStudy();
        StudyMember member = memberRepository.findByUserAndStudy(userId,
                study.getId())
            .orElseThrow(NotJoinedMemberException::new);

        validator.checkJoinedMember(member, study.getId());

        return postConverter.postToInfo(post);
    }

}
