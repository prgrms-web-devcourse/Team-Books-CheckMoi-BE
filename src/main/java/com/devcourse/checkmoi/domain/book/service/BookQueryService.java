package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.global.model.SimplePage;
import org.springframework.data.domain.Pageable;

public interface BookQueryService {

    LatestAllBooks getAllTop(Pageable pageRequest);

    BookSpecification getById(Long bookId);

    BookSpecification getByIsbn(String isbn);
}
