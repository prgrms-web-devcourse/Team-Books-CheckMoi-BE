package com.devcourse.checkmoi.domain.comment.dto;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Edit;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import org.springframework.lang.Nullable;

public sealed interface CommentRequest permits Search, Create, Edit {

    record Search(
        @Nullable Long postId
    ) implements CommentRequest {

        @Builder
        public Search {
        }
    }

    record Create(
        @NotBlank(message = "댓글은 비어있을 수 없습니다")
        @Size(max = 500, message = "댓글은 500자 이내로 작성해 주세요")
        String content

    ) implements CommentRequest {

        @Builder
        public Create {

        }
    }

    record Edit(
        @NotBlank(message = "댓글은 비어있을 수 없습니다")
        @Size(max = 500, message = "댓글은 500자 이내로 작성해 주세요")
        String content
    ) implements CommentRequest {

        @Builder
        public Edit {
        }
    }
}
