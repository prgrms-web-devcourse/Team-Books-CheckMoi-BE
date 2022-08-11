package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfos;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import org.springframework.data.domain.Pageable;

public interface BookQueryService {

    LatestAllBooks getAllTop(SimplePage pageRequest);

    BookInfo getById(Long bookId);

    BookInfo getByIsbn(String isbn);

    BookInfos findAllByCondition(Search request, Pageable pageable);
}
