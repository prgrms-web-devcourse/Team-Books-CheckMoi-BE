package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.SimpleBook;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public sealed interface ReadBookResponse permits SimpleBook, BookSpecification, LatestAllBooks {

    record SimpleBook(
        Long id,
        String title,
        String image,
        String author,
        String publisher,
        @DateTimeFormat(iso = ISO.DATE, pattern = "yyyy/MM/dd")
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate pubDate, // TODO : 날짜 포맷 결정
        String isbn,
        String description,
        @DateTimeFormat(iso = ISO.DATE, pattern = "yyyy/MM/dd")
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate createAt // TODO : 날짜 포맷 결정
    ) implements ReadBookResponse {

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
        @DateTimeFormat(iso = ISO.DATE, pattern = "yyyy/MM/dd")
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate pubDate,
        String isbn,
        String description,
        @DateTimeFormat(iso = ISO.DATE, pattern = "yyyy/MM/dd")
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate createAt // TODO : 날짜 포맷
    ) implements ReadBookResponse {

        @Builder
        public BookSpecification {
        }
    }

    record LatestAllBooks(
        List<SimpleBook> books
    ) implements ReadBookResponse {

    }
}
