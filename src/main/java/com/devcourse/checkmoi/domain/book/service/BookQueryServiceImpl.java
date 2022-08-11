package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.converter.BookConverter;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfos;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.exception.BookNotFoundException;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookQueryServiceImpl implements BookQueryService {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;


    @Override
    public BookInfos findAllByCondition(Search search, Pageable pageable) {
        Page<BookInfo> bookInfos = bookRepository.findAllByCondition(search, pageable);

        return new BookInfos(
            bookInfos.getContent(),
            bookInfos.getTotalPages()
        );
    }

    @Override
    public LatestAllBooks getAllTop(Pageable pageRequest) {
        return new LatestAllBooks(
            bookRepository.findAllTop(pageRequest).stream()
                .map(bookConverter::bookToInfo)
                .toList(),
            bookRepository.findBooksByLatestStudy(pageRequest).stream()
                .map(bookConverter::bookToInfo)
                .toList()
        );
    }

    @Override
    public BookInfo getById(Long bookId) {
        return bookRepository.findById(bookId)
            .map(bookConverter::bookToInfo)
            .orElseThrow(() -> new BookNotFoundException(bookId.toString()));
    }

    @Override
    public BookInfo getByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
            .map(bookConverter::bookToInfo)
            .orElseThrow(() -> new BookNotFoundException(isbn));
    }
}
