package com.devcourse.checkmoi.domain.book.service;

import static java.util.stream.Collectors.toList;
import com.devcourse.checkmoi.domain.book.converter.BookConverter;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.exception.BookNotFoundException;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BookReaderImpl implements BookReader {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    public BookReaderImpl(BookRepository bookRepository,
        BookConverter bookConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
    }

    @Override
    public LatestAllBooks getAllTop(SimplePage pageRequest) {
        PageRequest page =
            PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize(),
                Sort.by(Direction.DESC, "id"));

        return new LatestAllBooks(
            bookRepository.findAllTop(page).stream()
                .map(bookConverter::bookToSimple)
                .collect(toList()));

    }

    @Override
    public BookSpecification getById(Long bookId) {
        return bookRepository.findById(bookId)
            .map(bookConverter::bookToSpecification)
            .orElseThrow(() -> new BookNotFoundException(bookId.toString()));
    }
}
