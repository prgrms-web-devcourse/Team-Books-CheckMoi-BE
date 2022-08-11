package com.devcourse.checkmoi.domain.comment.facade;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.checkmoi.domain.comment.service.CommentQueryService;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryFacadeImpl implements
    CommentQueryFacade {

    private final CommentQueryService commentQueryService;

    private final PostQueryService postQueryService;

    @Override
    public Comments findAllComments(Long userId, Search request, Pageable pageable) {
        postQueryService.findByPostId(userId, request.postId());
        return commentQueryService.findAllComments(request, pageable);
    }
}
