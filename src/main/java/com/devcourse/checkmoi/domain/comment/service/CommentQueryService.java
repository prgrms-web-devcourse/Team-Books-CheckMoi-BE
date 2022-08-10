package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentQueryService {

    Comments findAllComments(Search request, Pageable pageable);
}
