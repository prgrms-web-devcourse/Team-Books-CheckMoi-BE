package com.devcourse.checkmoi.domain.book.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
import com.devcourse.checkmoi.domain.study.stub.StudyStub;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.domain.user.stub.UserStub;
import com.devcourse.checkmoi.template.RepositoryTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;


class BookRepositoryTest extends RepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

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

        whaleBook = Book.builder().author("고래").isbn("1231231231232").description("고래가 좋아")
            .publisher("Hanbit").thumbnail("abc/whale.jpeg")
            .publishedAt(new PublishedDate("20021011")).title("고래 무엇인가").build();

        bookRepository.save(birdBook);
        bookRepository.save(whaleBook);
    }

    @Test
    @DisplayName("페이지네이션 정보를 받아 해당하는 페이지의 Book 리스트를 가져온다")
    void createBook() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Direction.DESC, "id"));

        List<Book> topBooks = bookRepository.findAllTop(pageable);

        Assertions.assertThat(topBooks.size())
            .isEqualTo(1);
    }

    @Nested
    @DisplayName("책 목록 반환 값 변경 #90")
    class FindBooksByLatestStudyTest {

        @BeforeEach
        void setUp() {
            User user = UserStub.user();
            Study study = StudyStub.study(birdBook);

            userRepository.save(user);
            studyRepository.save(study);

        }

        @Test
        @DisplayName("S 최근에 스터디가 생성된 책을 조회")
        void findBooksByLatestStudy() {
            Pageable pageable = PageRequest.of(0, 1, Sort.by(Direction.DESC, "id"));

            List<Book> got = bookRepository.findBooksByLatestStudy(pageable);

            assertThat(got.get(0)).usingRecursiveComparison().isEqualTo(birdBook);
        }
    }

}