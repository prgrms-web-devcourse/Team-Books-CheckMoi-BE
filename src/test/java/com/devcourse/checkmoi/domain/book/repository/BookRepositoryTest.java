package com.devcourse.checkmoi.domain.book.repository;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
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
        birdBook = bookRepository.save(makeBook());
        whaleBook = bookRepository.save(makeBook());
    }

    @Test
    @DisplayName("페이지네이션 정보를 받아 해당하는 페이지의 Book 리스트를 가져온다")
    void createBook() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Direction.DESC, "id"));

        List<Book> topBooks = bookRepository.findAllTop(pageable);

        Assertions.assertThat(topBooks)
            .hasSize(1);
    }

    @Nested
    @DisplayName("책 목록 반환 값 변경 #90")
    class FindBooksByLatestStudyTest {

        @BeforeEach
        void setUp() {
            userRepository.save(makeUser());
            studyRepository.save(makeStudy(birdBook, StudyStatus.RECRUITING));
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
