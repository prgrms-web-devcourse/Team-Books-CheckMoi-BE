package com.devcourse.checkmoi.domain.book.repository;

import com.devcourse.checkmoi.domain.book.model.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomBookRepository {

    List<Book> findBooksByLatestStudy(Pageable page);
}
