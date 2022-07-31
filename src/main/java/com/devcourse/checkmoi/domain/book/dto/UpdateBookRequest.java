package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public sealed interface UpdateBookRequest permits CreateBook {

    // TODO : api 로 받아온 데이터들에 대해서는 어떤 검증조건이 있어야 할지 아직 미정상태
    record CreateBook(
        String title,
        String image,
        String author,
        String publisher,
        @JsonProperty(value = "pubdate")
        String pubDate,
        String isbn,
        String description
    ) implements UpdateBookRequest {

        @Builder
        public CreateBook {
        }
    }
}
