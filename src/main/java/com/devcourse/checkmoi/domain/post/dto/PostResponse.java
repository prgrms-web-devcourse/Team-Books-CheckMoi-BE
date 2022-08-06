package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfos;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.data.domain.Page;

public sealed interface PostResponse permits PostInfo, PostInfos {

    record PostInfo(
        Long id,
        String title,
        String content,
        String category,
        Long studyId,
        Long userId,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate createdAt,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate updatedAt
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
