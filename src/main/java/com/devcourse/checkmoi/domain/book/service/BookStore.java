package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;

public interface BookStore {

    SimpleBook save(CreateBook bookRequest);
}
