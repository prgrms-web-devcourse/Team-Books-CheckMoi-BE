package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;

public sealed interface CommentResponse permits CommentInfo {

    record CommentInfo(
        Long id,
        Long userId,
        Long postId,
        String content,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime updatedAt
    ) implements CommentResponse {

        @Builder
        public CommentInfo {
        }
    }
}
