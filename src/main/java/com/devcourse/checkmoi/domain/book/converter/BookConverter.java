package com.devcourse.checkmoi.domain.book.converter;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public SimpleBook bookToSimple(Book book) {
        return SimpleBook.builder()
            .id(book.getId())
            .title(book.getTitle())
            .author(book.getAuthor())
            .publisher(book.getPublisher())
            .category(book.getCategory().name())
            .isbn(book.getIsbn())
            .description(book.getDescription())
            .image(book.getThumbnail())
            .createAt(book.getCreatedAt())
            .pubDate(book.getPublishedAt().getPublishedAt())
            .build();
    }

    public Book CreateToBook(CreateBook createRequest) {
        return Book.builder()
            .author(createRequest.author())
            .isbn(createRequest.isbn())
            .publisher(createRequest.publisher())
            .description(createRequest.description())
            .title(createRequest.title())
            .thumbnail(createRequest.image())
            .publishedAt(new PublishedDate(createRequest.pubDate()))
            .build();
    }

    public BookSpecification bookToSpecification(Book book) {
        return BookSpecification.builder()
            .author(book.getAuthor())
            .description(book.getDescription())
            .image(book.getThumbnail())
            .isbn(book.getIsbn())
            .id(book.getId())
            .createAt(book.getCreatedAt())
            .pubDate(book.getPublishedAt().getPublishedAt())
            .publisher(book.getPublisher())
            .title(book.getTitle())
            .build();
    }
}
