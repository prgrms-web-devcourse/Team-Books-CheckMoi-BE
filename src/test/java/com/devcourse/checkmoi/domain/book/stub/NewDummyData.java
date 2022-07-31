package com.devcourse.checkmoi.domain.book.stub;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record NewDummyData(String author, String title, String thumbnail, Long bookId,
                           String description, String isbn, String publisher,
                           String publishedAt) {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public SimpleBook simple() {
        return SimpleBook.builder()
            .author(this.author)
            .title(this.title)
            .image(this.thumbnail)
            .description(this.description)
            .id(this.bookId)
            .description(this.description)
            .isbn(this.isbn)
            .publisher(this.publisher)
            .pubDate(LocalDate.parse(this.publishedAt,
                formatter))
            .build();
    }

    public CreateBook create() {
        return CreateBook.builder()
            .author(this.author)
            .description(this.description)
            .isbn(this.isbn)
            .pubDate(this.publishedAt)
            .publisher(this.publisher)
            .image(this.thumbnail)
            .title(this.title)
            .build();
    }


    public Book book() {
        return Book.builder()
            .author(this.author)
            .title(this.title)
            .thumbnail(this.thumbnail)
            .id(this.bookId)
            .description(this.description)
            .isbn(this.isbn)
            .publisher(this.publisher)
            .publishedAt(new PublishedDate(this.publishedAt))
            .build();
    }

    public BookSpecification specification() {
        return BookSpecification.builder()
            .author(this.author)
            .isbn(this.isbn)
            .publisher(this.publisher)
            .pubDate(LocalDate.parse(this.publishedAt,
                formatter))
            .id(this.bookId)
            .image(this.thumbnail)
            .description(this.description)
            .title(this.title)
            .build();
    }
}
