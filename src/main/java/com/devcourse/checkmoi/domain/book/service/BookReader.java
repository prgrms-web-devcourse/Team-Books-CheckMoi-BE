package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;

public interface BookReader {

    LatestAllBooks getAllTop(SimplePage pageRequest);

    BookSpecification getById(Long bookId);
}
