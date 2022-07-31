package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.SimpleBook;

public interface BookCommandService {

    SimpleBook save(CreateBook bookRequest);
}
