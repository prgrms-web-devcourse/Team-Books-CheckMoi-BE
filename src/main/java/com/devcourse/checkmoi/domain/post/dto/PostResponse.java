package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.Posts;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public sealed interface PostResponse permits PostInfo, Posts {

    record PostInfo(
        Long id,
        String title,
        String content,
        PostCategory category,
        Long studyId,
        Long writerId,
        String writer,
        String writerImage,
        Integer commentCount,

        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime updatedAt
    ) implements PostResponse {

        @Builder
        public PostInfo {
        }
    }

    record Posts(
        long totalPage,
        List<PostInfo> posts
    ) implements PostResponse {

    }

}
