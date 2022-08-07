package com.devcourse.checkmoi.domain.comment.converter;

import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public CommentInfo commentToInfo(Comment comment) {
        return CommentInfo.builder()
            .id(comment.getId())
            .userId(comment.getUser().getId())
            .postId(comment.getPost().getId())
            .content(comment.getContent())
            .createdAt(convertLocalDate(comment.getCreatedAt()))
            .updatedAt(convertLocalDate(comment.getUpdatedAt()))
            .build();
    }

    private LocalDate convertLocalDate(LocalDateTime time) {
        return time == null ? LocalDate.now() : time.toLocalDate();
    }
}
