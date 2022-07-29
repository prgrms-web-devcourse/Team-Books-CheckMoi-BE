package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest;

public interface BookService {

    ReadBookResponse.Simple save(UpdateBookRequest.CreateBook createRequest);

    ReadBookResponse.LatestAll getAllTop(SimplePage pageRequest);

    ReadBookResponse.Specification getById(Long bookId);
}
