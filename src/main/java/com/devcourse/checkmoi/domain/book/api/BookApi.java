package com.devcourse.checkmoi.domain.book.api;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.service.BookReader;
import com.devcourse.checkmoi.domain.book.service.BookStore;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookApi {

    private final BookStore bookStore;

    private final BookReader bookReader;

    public BookApi(BookStore bookStore,
        BookReader bookReader) {
        this.bookStore = bookStore;
        this.bookReader = bookReader;
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<Long>> updateBook(
        @RequestBody CreateBook createRequest) {
        Long bookId = bookStore.save(createRequest).id();

        return ResponseEntity.ok(
            new SuccessResponse<>(bookId)
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<LatestAllBooks>> topBooks() {
        LatestAllBooks topBooks = bookReader.getAllTop(SimplePage.defaultPage());

        return ResponseEntity.ok(
            new SuccessResponse<>(topBooks)
        );
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<SuccessResponse<BookSpecification>> getById(
        @PathVariable Long bookId) {
        BookSpecification bookSpecification = bookReader.getById(bookId);

        return ResponseEntity.ok(
            new SuccessResponse<>(bookSpecification)
        );
    }
}
