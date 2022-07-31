package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;

public interface BookReader {

    LatestAllBooks getAllTop(SimplePage pageRequest);

    BookSpecification getById(Long bookId);
}
