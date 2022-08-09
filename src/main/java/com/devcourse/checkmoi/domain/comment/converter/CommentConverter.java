package com.devcourse.checkmoi.domain.comment.converter;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.user.model.User;
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

    public Comment createToComment(Create request, Long postId, Long writerId) {
        return Comment.builder()
            .content(request.content())
            .post(
                Post.builder()
                .id(postId)
                .build()
            )
            .user(
                User.builder()
                .id(writerId)
                .build()
            )
            .build();
    }
}
