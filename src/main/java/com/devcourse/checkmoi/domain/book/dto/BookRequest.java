package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.global.annotation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;
import org.springframework.lang.Nullable;

public sealed interface BookRequest permits CreateBook, Search {

    record Search(
        // 검색조건
        @Nullable Long bookId,
        @Nullable Long studyId,
        @ValueOfEnum(codeMappingEnumClass = StudyStatus.class)
        @Nullable String studyStatus,

        // 정렬조건
        @Nullable Boolean latestStudy,
        @Nullable Boolean mostStudy
    ) implements BookRequest {

        @Builder
        public Search {
        }
    }

    record CreateBook(
        @NotBlank(message = "책 제목은 비어있을 수 없습니다.")
        @Size(max = 150, message = "타이틀 길이는 150자를 넘을 수 없습니다")
        String title,

        @URL(message = "올바른 이미지 URL이 필요합니다.")
        @Size(max = 1000, message = "썸네일 URL의 길이는 1,000자를 넘을 수 없습니다.")
        String image,

        @Size(max = 50, message = "저자 이름은 50자를 넘을 수 없습니다.")
        String author,

        @Size(max = 50, message = "출판사 이름은 50자를 넘을 수 없습니다.")
        String publisher,
        @JsonProperty(value = "pubdate")
        String pubDate,
        @Size(max = 20, message = "isbn은 20자를 넘길 수 없습니다.")
        String isbn,
        @Size(max = 10000, message = "책 설명은 10,000자를 넘을 수 없습니다.")
        String description
    ) implements BookRequest {

        @Builder
        public CreateBook {
        }
    }
}
