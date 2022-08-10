package com.devcourse.checkmoi.domain.comment.service;

import com.devcourse.checkmoi.domain.comment.converter.CommentConverter;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Edit;
import com.devcourse.checkmoi.domain.comment.exception.CommentNotFoundException;
import com.devcourse.checkmoi.domain.comment.model.Comment;
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
        // TODO: validation 댓글을 작성한 본인과 스터디장만 삭제할 수 있다
        // TODO: 스터디 종료상태인지 확인한다
        commentRepository.deleteById(commentId);
    }

    @Override
    public Long createComment(Long postId, Long userId, Create request) {
        // TODO: validation : 스터디 종료상태인지 확인한다
        // TODO: 스터디원만 댓글을 작성할 수 있다
        return commentRepository.save(
            commentConverter.createToComment(request, postId, userId)
        ).getId();
    }

    @Override
    public void editComment(Long userId, Long commentId, Edit request) {
        // TODO: validation 댓글을 작성한 본인만 수정할 수 있다.
        // TODO: 스터디 종료 상태인지 확인한다

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(CommentNotFoundException::new);

        comment.editComment(request.content());
    }

}
