package com.devcourse.checkmoi.domain.comment.converter;

import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public CommentInfo commentToInfo(Comment comment) {
        return CommentInfo.builder()
            .id(comment.getId())
            .userId(comment.getUser().getId())
            .postId(comment.getPost().getId())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();
    }

}
