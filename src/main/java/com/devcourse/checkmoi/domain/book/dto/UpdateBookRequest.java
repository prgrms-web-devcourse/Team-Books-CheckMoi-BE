package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;
import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface UpdateBookRequest permits CreateBook {

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

    }
}
