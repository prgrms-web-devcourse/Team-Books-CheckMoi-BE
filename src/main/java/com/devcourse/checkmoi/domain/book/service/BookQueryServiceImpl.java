package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.converter.BookConverter;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.domain.book.exception.BookNotFoundException;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BookQueryServiceImpl implements BookQueryService {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    public BookQueryServiceImpl(BookRepository bookRepository,
        BookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }

    @Override
    public LatestAllBooks getAllTop(Pageable pageRequest) {
        return new LatestAllBooks(
            bookRepository.findAllTop(pageRequest).stream()
                .map(bookConverter::bookToSimple)
                .toList(),
            bookRepository.findBooksByLatestStudy(pageRequest).stream()
                .map(bookConverter::bookToSimple)
                .toList()
        );

    }

    @Override
    public BookSpecification getById(Long bookId) {
        return bookRepository.findById(bookId)
            .map(bookConverter::bookToSpecification)
            .orElseThrow(() -> new BookNotFoundException(bookId.toString()));
    }

    @Override
    public BookSpecification getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
            .map(bookConverter::bookToSpecification)
            .orElseThrow(() -> new BookNotFoundException(isbn));
    }
}
