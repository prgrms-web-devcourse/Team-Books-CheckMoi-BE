package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public sealed interface CommentResponse permits CommentInfo, Comments {

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

    record Comments(
        List<CommentInfo> comments,
        long totalPage

    ) implements CommentResponse {

        @Builder
        public Comments {
        }
    }
}
