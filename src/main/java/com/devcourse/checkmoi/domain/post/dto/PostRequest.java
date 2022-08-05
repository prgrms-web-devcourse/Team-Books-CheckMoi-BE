package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import lombok.Builder;

public sealed interface PostRequest permits Search, Create, Edit {

    record Search(
        Long id
    ) implements PostRequest {

        @Builder
        public Search {
        }
    }

    record Create(
        String title,
        String content,
        Long studyId
    ) implements PostRequest {

        @Builder
        public Create {
        }
    }

    record Edit(
        String title,
        String content,
        Long studyId
    ) implements PostRequest {

        @Builder
        public Edit {
        }
    }
}
