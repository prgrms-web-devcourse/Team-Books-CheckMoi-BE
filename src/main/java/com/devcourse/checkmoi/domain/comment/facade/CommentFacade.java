package com.devcourse.checkmoi.domain.comment.facade;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.checkmoi.domain.comment.service.CommentCommandService;
import com.devcourse.checkmoi.domain.comment.service.CommentQueryService;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentFacade {

    private final StudyQueryService studyQueryService;

    private final PostQueryService postQueryService;

    private final CommentQueryService commentQueryService;

    private final CommentCommandService commentCommandService;

    public Long createComment(Long postId, Long userId, Create request) {
        PostInfo post = postQueryService.findByPostId(userId, postId);
        studyQueryService.validateOngoingStudy(post.studyId());
        studyQueryService.validateParticipateUser(post.studyId(), userId);

        return commentCommandService.createComment(postId, userId, request);
    }

    public Comments findAllComments(Long userId, Search request, Pageable pageable) {
        postQueryService.findByPostId(userId, request.postId());
        return commentQueryService.findAllComments(request, pageable);
    }
}
