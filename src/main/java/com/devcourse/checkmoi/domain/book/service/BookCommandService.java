package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;

public interface BookCommandService {

    BookInfo save(CreateBook bookRequest);
}
