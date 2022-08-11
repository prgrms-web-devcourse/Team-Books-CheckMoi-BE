package com.devcourse.checkmoi.domain.comment.facade;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import org.springframework.data.domain.Pageable;

public interface CommentQueryFacade {

    Comments findAllComments(Long userId, Search request, Pageable pageable);
}
