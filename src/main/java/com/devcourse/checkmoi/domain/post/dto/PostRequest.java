package com.devcourse.checkmoi.domain.post.dto;

import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.global.annotation.ValueOfEnum;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;

public sealed interface PostRequest permits Search, Create, Edit {

    record Search(
        @NotNull(message = "게시글을 조회하려는 스터디 식별정보를 알려주세요") Long studyId,
        @ValueOfEnum(codeMappingEnumClass = PostCategory.class, message = "카테고리는 NOTICE 또는 GENERAL 이어야 합니다") String category
    ) implements PostRequest {

        @Builder
        public Search {
        }
    }

    record Create(
        @Size(max = 50, message = "게시글 제목은 50자 이내로 작성해 주세요")
        String title,
        @Size(max = 6000, message = "게시글 본문은 6,000자 이내로 작성해 주세요")
        String content,
        @ValueOfEnum(codeMappingEnumClass = PostCategory.class) @Size(max = 20, message = "카테고리 이름은 20자 이내여야 합니다")
        String category,
        Long studyId
    ) implements PostRequest {

        @Builder
        public Create {
        }
    }

    record Edit(
        @Size(max = 50, message = "게시글 제목은 50자 이내로 작성해 주세요")
        String title,
        @Size(max = 6000, message = "게시글 본문은 6,000자 이내로 작성해 주세요")
        String content,
        Long studyId
    ) implements PostRequest {

        @Builder
        public Edit {
        }
    }
}
