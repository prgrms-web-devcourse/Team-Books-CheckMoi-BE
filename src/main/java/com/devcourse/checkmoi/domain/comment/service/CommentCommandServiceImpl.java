package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.converter.CommentConverter;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;

    private final CommentConverter commentConverter;

    @Override
    public void deleteById(Long userId, Long commentId) {
        // TODO: validation
        commentRepository.deleteById(commentId);
    }

    @Override
    public Long createComment(Long postId, Long userId, Create request) {
        return commentRepository.save(
            commentConverter.createToComment(request, postId, userId)
        ).getId();
    }

}
