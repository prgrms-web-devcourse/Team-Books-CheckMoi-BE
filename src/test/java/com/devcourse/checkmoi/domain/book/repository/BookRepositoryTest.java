package com.devcourse.checkmoi.domain.book.repository;

import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.template.RepositoryTest;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

        assertThat(topBooks)
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

            AssertionsForClassTypes.assertThat(got.get(0)).usingRecursiveComparison()
                .isEqualTo(birdBook);
        }
    }

    @Nested
    @DisplayName("스터디 조회 v2 #158")
    class SearchStudiesTest {

        private List<Book> books = new ArrayList<>();

        private List<Study> studies = new ArrayList<>();

        @BeforeEach
        void setUp() throws InterruptedException {
            tearDown();

            books.add(bookRepository.save(makeBook()));
            sleep(1000);
            books.add(bookRepository.save(makeBook()));
            sleep(1000);
            books.add(bookRepository.save(makeBook()));

            // book1 study
            studies.add(studyRepository.save(makeStudy(books.get(0), RECRUITING)));
            studies.add(studyRepository.save(makeStudy(books.get(0), IN_PROGRESS)));
            studies.add(studyRepository.save(makeStudy(books.get(0), FINISHED)));

            // book2 study
            studies.add(studyRepository.save(makeStudy(books.get(1), IN_PROGRESS)));
            studies.add(studyRepository.save(makeStudy(books.get(1), FINISHED)));

            // book3 study
            // NONE
        }

        @AfterEach
        void tearDown() {
            studyRepository.deleteAllInBatch();
            bookRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("S 현재 책에 대해서 진행중인 스터디가 있는 책을 찾을 수 있다.")
        void searchStudies() {
            Search search = Search.builder()
                .bookId(books.get(0).getId())
                .studyStatus(IN_PROGRESS.toString())
                .build();

            SimplePage page = SimplePage.builder().build();

            Page<BookInfo> result = bookRepository.findAllByCondition(search, page.pageRequest());

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("S 조건이 없다면 페이지 만큼 책을 조회한다")
        void searchStudies2() {
            Search search = Search.builder().build();

            SimplePage page = SimplePage.builder().build();

            Page<BookInfo> result = bookRepository.findAllByCondition(search, page.pageRequest());
            assertThat(result.getContent()).hasSize(3);
        }

        @Test
        @DisplayName("S 정렬조건에 의해서 정렬된 데이터가 나온다")
        void searchStudies3() {
            Search search = Search.builder()
                .mostStudy(true)
                .latestStudy(true)
                .build();

            SimplePage page = SimplePage.builder().build();

            Page<BookInfo> result = bookRepository.findAllByCondition(search, page.pageRequest());
            assertThat(result.getContent()).hasSize(3);
        }
    }
}
