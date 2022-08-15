package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfos;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public sealed interface BookResponse permits BookInfo, BookInfos, LatestAllBooks {

    record BookInfo(
        Long id,
        String title,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate pubdate,
        String isbn,
        String image,
        String description,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime createdAt
    ) implements BookResponse {

        @Builder
        public BookInfo {
        }
    }

    record BookInfos(
        List<BookInfo> books,
        long totalPage
    ) implements BookResponse {

        @Builder
        public BookInfos {

        }
    }

    record LatestAllBooks(
        List<BookInfo> latestBooks,
        List<BookInfo> studyLatestBooks

    ) implements BookResponse {

    }
}
