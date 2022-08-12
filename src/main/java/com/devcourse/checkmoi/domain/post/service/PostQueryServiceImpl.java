package com.devcourse.checkmoi.domain.post.service;

import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.Posts;
import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.post.service.validator.PostServiceValidator;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.global.model.SimplePage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;
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

    @Override
    public Posts findAllByCondition(Long userId, Search request, SimplePage page) {
        memberRepository.findByUserAndStudy(userId, request.studyId())
            .orElseThrow(NotJoinedMemberException::new);

        PageRequest pageRequest = PageRequest.of(
            page.getPage() - 1,
            page.getSize(),
            Sort.by(new Order(page.getDirection(), "createdAt", NullHandling.NATIVE)));

        return postRepository.findAllByCondition(
            userId,
            request,
            pageRequest
        );
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
