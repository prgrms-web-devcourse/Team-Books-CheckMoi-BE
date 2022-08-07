package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.converter.CommentConverter;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    @Override
    public List<CommentInfo> findAllComments(Long userId, Search request) {
        return commentRepository.findAllByCondition(userId, request);
    }
}
