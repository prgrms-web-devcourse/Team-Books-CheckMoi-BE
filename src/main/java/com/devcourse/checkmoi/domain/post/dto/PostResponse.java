package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;

public sealed interface PostResponse permits PostInfo {

    record PostInfo(
        Long id,
        String title,
        String content,
        String category,
        Long studyId,
        Long writerId,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate createdAt,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate updatedAt
    ) implements PostResponse {

        @Builder
        public PostInfo {
        }
    }

}
