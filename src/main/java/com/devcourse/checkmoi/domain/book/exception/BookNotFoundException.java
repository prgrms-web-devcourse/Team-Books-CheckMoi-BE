package com.devcourse.checkmoi.domain.book.exception;

import com.devcourse.checkmoi.global.exception.EntityNotFoundException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;

public class BookNotFoundException extends EntityNotFoundException {

    public BookNotFoundException(String message) {
        super(message, ErrorMessage.BOOK_NOT_FOUND);
    }
}
