package com.devcourse.checkmoi.domain.book.repository;

import com.devcourse.checkmoi.domain.book.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository{

    @Query("select book From Book book")
    List<Book> findAllTop(Pageable pageable);

    Optional<Book> findByIsbn(String isbn);
}
