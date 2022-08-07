package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;

public sealed interface CommentResponse permits CommentInfo {

    record CommentInfo(
        Long id,
        Long userId,
        String content,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate createdAt,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate updatedAt
    ) implements CommentResponse {

        @Builder
        public CommentInfo {
        }
    }
}
