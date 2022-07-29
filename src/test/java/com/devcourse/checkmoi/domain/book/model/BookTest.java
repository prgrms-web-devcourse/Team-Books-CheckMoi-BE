package com.devcourse.checkmoi.domain.book.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    @DisplayName("모든 정보를 문자열로 담은 책 정보를 전달받았을 때 Book 객체를 생성하는 테스트")
    void test() {
        String title = "title01";
        String author = "author1";
        String publisher = "Hanbit";
        String isbn = "1121121121123";
        String thumbnail = "ab/url";
        String publishedAt = "22011020";

        Book book = Book.builder()
            .author(author)
            .title(title)
            .publishedAt(new PublishedDate(publishedAt))
            .isbn(isbn)
            .thumbnail(thumbnail)
            .publisher(publisher)
            .build();

        Assertions.assertThat(book.getPublishedAt().getPublishedAt())
            .isEqualTo(LocalDate.parse(publishedAt, DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}