package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import lombok.Builder;

public sealed interface CommentRequest permits Search {

    record Search(
        Long postId
    ) implements CommentRequest {

        @Builder
        public Search {
        }
    }
}
