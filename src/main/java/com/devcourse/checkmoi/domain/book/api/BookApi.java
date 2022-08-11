package com.devcourse.checkmoi.domain.book.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.domain.book.service.BookCommandService;
import com.devcourse.checkmoi.domain.book.service.BookQueryService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookApi {

    private final BookCommandService bookCommandService;

    private final BookQueryService bookQueryService;

    public BookApi(BookCommandService bookCommandService, BookQueryService bookQueryService) {
        this.bookCommandService = bookCommandService;
        this.bookQueryService = bookQueryService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<Long>> register(
        @Valid @RequestBody CreateBook createRequest
    ) {
        Long bookId = bookCommandService.save(createRequest).id();

        return ResponseEntity
            .created(generatedUri(bookId))
            .body(new SuccessResponse<>(bookId));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<LatestAllBooks>> topBooks() {
        SimplePage pageRequest = SimplePage.builder()
            .page(1)
            .size(4)
            .build();
        LatestAllBooks topBooks = bookQueryService.getAllTop(pageRequest.pageRequest());

        return ResponseEntity.ok(
            new SuccessResponse<>(topBooks)
        );
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<SuccessResponse<BookSpecification>> getById(
        @PathVariable Long bookId
    ) {
        BookSpecification bookSpecification = bookQueryService.getById(bookId);

        return ResponseEntity.ok(
            new SuccessResponse<>(bookSpecification)
        );
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<SuccessResponse<BookSpecification>> getByIsbn(@PathVariable String isbn) {
        BookSpecification book = bookQueryService.getByIsbn(isbn);

        return ResponseEntity.ok(
            new SuccessResponse<>(book)
        );
    }

}
