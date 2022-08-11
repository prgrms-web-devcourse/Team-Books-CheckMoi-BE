package com.devcourse.checkmoi.domain.book.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfos;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.service.BookCommandService;
import com.devcourse.checkmoi.domain.book.service.BookQueryService;
import com.devcourse.checkmoi.global.model.PageRequest;
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
@RequestMapping("/api")
public class BookApi {

    private final BookCommandService bookCommandService;

    private final BookQueryService bookQueryService;

    public BookApi(BookCommandService bookCommandService, BookQueryService bookQueryService) {
        this.bookCommandService = bookCommandService;
        this.bookQueryService = bookQueryService;
    }

    @PostMapping("/books")
    public ResponseEntity<SuccessResponse<Long>> register(
        @Valid @RequestBody CreateBook createRequest
    ) {
        Long bookId = bookCommandService.save(createRequest).id();

        return ResponseEntity
            .created(generatedUri(bookId))
            .body(new SuccessResponse<>(bookId));
    }

    @GetMapping("/books")
    public ResponseEntity<SuccessResponse<LatestAllBooks>> topBooks() {
        LatestAllBooks topBooks = bookQueryService.getAllTop(SimplePage.defaultPage());

        return ResponseEntity.ok(
            new SuccessResponse<>(topBooks)
        );
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<SuccessResponse<BookInfo>> getById(
        @PathVariable Long bookId
    ) {
        BookInfo bookSpecification = bookQueryService.getById(bookId);

        return ResponseEntity.ok(
            new SuccessResponse<>(bookSpecification)
        );
    }

    @GetMapping("/books/isbn/{isbn}")
    public ResponseEntity<SuccessResponse<BookInfo>> getByIsbn(@PathVariable String isbn) {
        BookInfo book = bookQueryService.getByIsbn(isbn);

        return ResponseEntity.ok(
            new SuccessResponse<>(book)
        );
    }

    /********************************* API v2  ****************************************/

    @GetMapping("/v2/books")
    public ResponseEntity<SuccessResponse<BookInfos>> findAllByCondition(
        Search request,
        PageRequest simplePage
    ) {
        BookInfos books = bookQueryService.findAllByCondition(request, simplePage.of());

        return ResponseEntity.ok()
            .body(new SuccessResponse<>(books));
    }

}
