package com.devcourse.checkmoi.domain.book.converter;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public BookInfo bookToInfo(Book book) {
        return BookInfo.builder()
            .id(book.getId())
            .title(book.getTitle())
            .author(book.getAuthor())
            .publisher(book.getPublisher())
            .isbn(book.getIsbn())
            .description(book.getDescription())
            .image(book.getThumbnail())
            .createdAt(book.getCreatedAt())
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

}
