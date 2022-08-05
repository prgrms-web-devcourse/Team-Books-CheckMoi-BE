package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.SimpleBook;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public sealed interface BookResponse permits SimpleBook, BookSpecification, LatestAllBooks {

    record SimpleBook(
        Long id,
        String title,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate pubDate, // TODO : 날짜 포맷 결정
        String isbn,
        String image,
        String description,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime createdAt // TODO : 날짜 포맷
    ) implements BookResponse {

        @Builder
        public SimpleBook {
        }
    }

    record BookSpecification(
        Long id,
        String title,
        String image,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate pubDate,
        String isbn,
        String description,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime createdAt // TODO : 날짜 포맷
    ) implements BookResponse {

        @Builder
        public BookSpecification {
        }
    }

    record LatestAllBooks(
        List<SimpleBook> latestBooks,
        List<SimpleBook> studyLatestBooks

    ) implements BookResponse {

    }
}
