package com.devcourse.checkmoi.domain.book.service;

import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.LatestAll;
import com.devcourse.checkmoi.domain.book.dto.ReadBookResponse.Simple;
import com.devcourse.checkmoi.domain.book.dto.SimplePage;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest;
import com.devcourse.checkmoi.domain.book.dto.UpdateBookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private Book birdBook;
    private Book whaleBook;

    @BeforeEach
    void setUp() {
        birdBook = Book.builder()
            .author("제비")
            .isbn("1231231231231")
            .description("제비가 좋아")
            .publisher("Hanbit")
            .thumbnail("abc/jebi.jpeg")
            .publishedAt(new PublishedDate("20021011"))
            .title("제비란 무엇인가")
            .build();

        whaleBook = Book.builder()
            .author("고래")
            .isbn("1231231231232")
            .description("고래가 좋아")
            .publisher("Hanbit")
            .thumbnail("abc/whale.jpeg")
            .publishedAt(new PublishedDate("20021011"))
            .title("고래 무엇인가")
            .build();

        bookRepository.save(birdBook);
        bookRepository.save(whaleBook);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAllInBatch();
    }

    @Nested
    public class UpdateTest {

        @Test
        @DisplayName("이미 등록되어 있는 책에 대한 등록 요청 시 서버에 존재하는 리소스의 id 를 포함한 정보를 리턴한다")
        void returnIdOfExistingBook() {
            UpdateBookRequest.CreateBook createRequest = new CreateBook(
                birdBook.getTitle(),
                birdBook.getThumbnail(),
                birdBook.getAuthor(),
                birdBook.getPublisher(),
                "22011020",
                "1231231231234",
                "오랜 역사에 대한 책"
            );

            ReadBookResponse.Simple response = bookService.save(createRequest);

            Assertions.assertThat(response.id())
                .isEqualTo(birdBook.getId());
        }

        @Test
        @DisplayName("등록되어 있지 않은 책에 대한 등록 요청 시 생성된 리소스의 id 를 포함한 정보를 리턴한다")
        void returnIdOfNewlyCreatedBook() {
            UpdateBookRequest.CreateBook createRequest = new CreateBook(
                "고양이란 무엇인가",
                "abc/cat.jpeg",
                "냥찬가",
                "Hanbit",
                "22011020",
                "1231231231235",
                "고양이의 귀여움을 설명한다"
            );

            Simple response = bookService.save(createRequest);

            Assertions.assertThat(response.id())
                .isNotNull();
        }
    }

    @Nested
    public class GetAllTopTest {

        private Book lastCatBook;

        @BeforeEach
        void setUp() {
            lastCatBook = Book.builder()
                .title("고양이란 무엇인가")
                .thumbnail("abc/cat.jpeg")
                .author("냥찬가")
                .publisher("Hanbit")
                .publishedAt(new PublishedDate("22011020"))
                .isbn("1231231231235")
                .description("고양이의 귀여움을 설명한다")
                .build();

            bookRepository.save(lastCatBook);
        }

        @Test
        @DisplayName("최신순으로 책 목록을 가져온다")
        void getTopBooks() {
            SimplePage simplePage = SimplePage.defaultPage();

            LatestAll allTopBooks = bookService.getAllTop(simplePage);

            Assertions.assertThat(allTopBooks.books().get(0).id())
                .isEqualTo(lastCatBook.getId());
        }
    }

}