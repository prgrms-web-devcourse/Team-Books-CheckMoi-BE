package com.devcourse.checkmoi.domain.book.converter;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public SimpleBook bookToSimple(Book book) {
        return SimpleBook.builder()
            .id(book.getId())
            .author(book.getAuthor())
            .isbn(book.getIsbn())
            .publisher(book.getPublisher())
            .description(book.getDescription())
            .title(book.getTitle())
            .image(book.getThumbnail())
            .createAt(book.getCreatedAt().toLocalDate())
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
            .createAt(book.getCreatedAt().toLocalDate())
            .pubDate(book.getPublishedAt().getPublishedAt())
            .publisher(book.getPublisher())
            .title(book.getTitle())
            .build();
    }
}
