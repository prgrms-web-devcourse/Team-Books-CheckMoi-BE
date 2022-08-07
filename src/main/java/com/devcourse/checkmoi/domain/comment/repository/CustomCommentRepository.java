package com.devcourse.checkmoi.domain.comment.repository;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import java.util.List;

public interface CustomCommentRepository {

    List<CommentInfo> findAllByCondition(Long userId, Search request);
}
