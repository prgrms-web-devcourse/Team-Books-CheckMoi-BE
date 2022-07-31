package com.devcourse.checkmoi.domain.book.api;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.service.BookCommandService;
import com.devcourse.checkmoi.domain.book.service.BookQueryService;
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

    private final BookCommandService bookCommandService;

    private final BookQueryService bookQueryService;

    public BookApi(BookCommandService bookCommandService,
        BookQueryService bookQueryService) {
        this.bookCommandService = bookCommandService;
        this.bookQueryService = bookQueryService;
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<Long>> register(
        @RequestBody CreateBook createRequest) {
        Long bookId = bookCommandService.save(createRequest).id();

        return ResponseEntity.ok(
            new SuccessResponse<>(bookId)
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<LatestAllBooks>> topBooks() {
        LatestAllBooks topBooks = bookQueryService.getAllTop(SimplePage.defaultPage());

        return ResponseEntity.ok(
            new SuccessResponse<>(topBooks)
        );
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<SuccessResponse<BookSpecification>> getById(
        @PathVariable Long bookId) {
        BookSpecification bookSpecification = bookQueryService.getById(bookId);

        return ResponseEntity.ok(
            new SuccessResponse<>(bookSpecification)
        );
    }
}