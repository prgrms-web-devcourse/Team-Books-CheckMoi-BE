package com.devcourse.checkmoi.domain.book.service;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.domain.book.exception.BookNotFoundException;
import com.devcourse.checkmoi.domain.book.model.Book;
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
class BookQueryServiceImplTest {

    @Autowired
    private BookQueryService bookQueryService;

    @Autowired
    private BookRepository bookRepository;

    private Book lastCatBook;

    @BeforeEach
    void setUp() {
        lastCatBook = makeBook();
        bookRepository.save(lastCatBook);
    }

    @AfterEach
    void deleteAll() {
        bookRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("상위 책 목록 가져오기 테스트")
    class GetAllTopTest {

        @Test
        @DisplayName("최신순으로 책 목록을 가져온다")
        void getTopBooks() {
            SimplePage simplePage = new SimplePage();
            simplePage.setSize(4);

            LatestAllBooks allTopBooks = bookQueryService.getAllTop(simplePage.of());

            Assertions.assertThat(allTopBooks.latestBooks().get(0).id())
                .isEqualTo(lastCatBook.getId());
        }

    }

    @Nested
    @DisplayName("책 정보를 통해 해당 책 하나를 가져온다")
    class GetTest {

        @Test
        @DisplayName("존재하지 않는 id 로 책을 조회할 경우 예외가 발생한다")
        void getByIdFail() {
            Long notExistingId = 0L;

            Assertions.assertThatThrownBy(() -> bookQueryService.getById(notExistingId))
                .isInstanceOf(BookNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("ISBN을 기준으로 책 단일 조회")
    class GetByIsbnTest {
        @Test
        @DisplayName("ISBN 기준으로 책을 조회한다.")
        void getByIsbn() {
            BookSpecification book = bookQueryService.getByIsbn(lastCatBook.getIsbn());

            Assertions.assertThat(book.id()).isEqualTo(lastCatBook.getId());
        }

        @Test
        @DisplayName("해당 책이 존재하지 않으면 예외를 발생시킨다.")
        void bookNotFound() {
            String wrongIsbn = "0000000000000";

            Assertions.assertThatThrownBy(() -> bookQueryService.getByIsbn(wrongIsbn))
                .isInstanceOf(BookNotFoundException.class);
        }
    }
}