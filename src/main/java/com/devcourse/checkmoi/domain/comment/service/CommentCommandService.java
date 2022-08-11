package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Edit;
import com.devcourse.checkmoi.domain.study.model.StudyMember;

public interface CommentCommandService {

    void deleteById(Long userId, Long commentId);

    Long createComment(Long postId, Long userId, Create request);

    void editComment(Long userId, Long commentId, Edit request);
}
