package com.devcourse.checkmoi.domain.book.stub;

import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import java.time.LocalDate;
import java.util.List;

public class BookStub {
    public static List<Book> StubBook() {
        return List.of(
            Book.builder()
                .id(1L)
                .title("자바")
                .description("자바책")
                .author("김자바")
                .publisher("자바출판")
                .isbn("0000000000000")
                .thumbnail("https://example.com/java.png")
                .publishedAt(new PublishedDate("20121111"))
                .build(),
            Book.builder()
                .id(2L)
                .title("자바스크립트")
                .description("자바스크립트책")
                .author("김자바스크립트")
                .publisher("자바스크립트출판")
                .isbn("0000000000001")
                .thumbnail("https://example.com/java.png")
                .publishedAt(new PublishedDate("20211201"))
                .build()
        );
    }
}
