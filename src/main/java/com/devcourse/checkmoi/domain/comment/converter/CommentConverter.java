package com.devcourse.checkmoi.domain.comment.converter;

import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.model.Comment;
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
            .createdAt(convertTime(comment.getCreatedAt()))
            .updatedAt(convertTime(comment.getUpdatedAt()))
            .build();
    }

    private LocalDateTime convertTime(LocalDateTime time) {
        return time == null ? LocalDateTime.now() : time;
    }
}
