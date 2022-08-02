package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.converter.BookConverter;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.exception.BookNotFoundException;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
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
    public LatestAllBooks getAllTop(SimplePage pageRequest) {
        PageRequest registerLatest =
            PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize(),
                Sort.by(Direction.DESC, "createdAt"));
        PageRequest studyLatest =
            PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize()
            );
        return new LatestAllBooks(
            bookRepository.findAllTop(registerLatest).stream()
                .map(bookConverter::bookToSimple)
                .toList(),
            bookRepository.findBooksByLatestStudy(studyLatest).stream()
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
}
