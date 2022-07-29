package com.devcourse.checkmoi.domain.book.repository;

import com.devcourse.checkmoi.domain.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
