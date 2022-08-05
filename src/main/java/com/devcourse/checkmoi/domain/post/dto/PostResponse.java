package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import lombok.Builder;

public sealed interface PostResponse permits PostInfo {

    record PostInfo(
        Long id
    ) implements PostResponse {

        @Builder
        public PostInfo {
        }
    }
}
