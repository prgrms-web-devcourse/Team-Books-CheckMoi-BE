package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;

    @Override
    public void deleteById(Long userId, Long commentId) {
        // TODO: validation
        commentRepository.deleteById(commentId);
    }
}
