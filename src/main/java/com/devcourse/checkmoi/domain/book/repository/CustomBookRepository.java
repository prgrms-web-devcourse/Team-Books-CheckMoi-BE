package com.devcourse.checkmoi.domain.book.repository;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBookRepository {

    List<Book> findBooksByLatestStudy(Pageable page);

    Page<BookInfo> findAllByCondition(Search search, Pageable pageable);
}
