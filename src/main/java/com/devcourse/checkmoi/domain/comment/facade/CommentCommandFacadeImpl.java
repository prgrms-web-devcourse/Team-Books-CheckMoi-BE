package com.devcourse.checkmoi.domain.comment.facade;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.service.CommentCommandService;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandFacadeImpl implements
    CommentCommandFacade {

    private final StudyQueryService studyQueryService;

    private final PostQueryService postQueryService;

    private final CommentCommandService commentCommandService;

    @Override
    public Long createComment(Long postId, Long userId, Create request) {
        PostInfo post = postQueryService.findByPostId(userId, postId);
        studyQueryService.ongoingStudy(post.studyId());
        studyQueryService.participateUser(post.studyId(), userId);

        return commentCommandService.createComment(postId, userId, request);
    }
}
