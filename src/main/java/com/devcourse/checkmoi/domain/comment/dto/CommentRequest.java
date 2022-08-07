package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import lombok.Builder;
import org.springframework.lang.Nullable;

public sealed interface CommentRequest permits Search {

    record Search(
        @Nullable Long postId
    ) implements CommentRequest {

        @Builder
        public Search {
        }
    }
}
