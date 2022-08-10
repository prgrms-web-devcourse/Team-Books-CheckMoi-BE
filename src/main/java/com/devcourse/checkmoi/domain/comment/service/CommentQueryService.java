package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import java.util.List;

public interface CommentQueryService {

    List<CommentInfo> findAllComments(Long userId, Search request);
}
