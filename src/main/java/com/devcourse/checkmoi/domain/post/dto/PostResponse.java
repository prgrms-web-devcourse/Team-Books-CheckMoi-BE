package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfos;
import lombok.Builder;
import org.springframework.data.domain.Page;

public sealed interface PostResponse permits PostInfo, PostInfos {

    record PostInfo(
        Long id
    ) implements PostResponse {

        @Builder
        public PostInfo {
        }
    }

    record PostInfos(
        Page<PostInfo> posts
    ) implements PostResponse {

        @Builder
        public PostInfos {

        }
    }
}
