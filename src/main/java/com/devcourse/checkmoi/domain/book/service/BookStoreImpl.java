package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.converter.BookConverter;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookStoreImpl implements BookStore {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    public BookStoreImpl(BookRepository bookRepository,
        BookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }

    @Override
    @Transactional
    public SimpleBook save(CreateBook bookRequest) {
        return bookRepository.findByIsbn(bookRequest.isbn())
            .map(bookConverter::bookToSimple)
            .orElseGet(() ->
                bookConverter.bookToSimple(this.create(bookRequest)));
    }

    private Book create(CreateBook createRequest) {
        return bookRepository.save(bookConverter.CreateToBook(createRequest));
    }
}
