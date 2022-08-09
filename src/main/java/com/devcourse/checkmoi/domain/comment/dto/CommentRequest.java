package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import lombok.Builder;
import org.springframework.lang.Nullable;

public sealed interface CommentRequest permits Search, Create {

    record Search(
        @Nullable Long postId
    ) implements CommentRequest {

        @Builder
        public Search {
        }
    }

    record Create(
        String content

    ) implements CommentRequest {

        @Builder
        public Create {

        }
    }
}
