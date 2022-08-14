package com.devcourse.checkmoi.domain.study.service.schedule;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithStudyDate;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.dto.ExpiredStudies;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyScheduleServiceTest {

    @Autowired
    private StudyScheduleService service;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private BookRepository bookRepository;

    private Study recruitingStudy;

    private Study toBeFinishedStudy;

    private Book book;

    @BeforeEach
    void setUp() {
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();

        book = makeBook();
        bookRepository.save(book);

        recruitingStudy = studyRepository.save(makeStudyWithStudyDate(book, StudyStatus.RECRUITING,
            LocalDate.of(2022, 8, 10), LocalDate.of(2022, 10, 20)));
        toBeFinishedStudy = studyRepository.save(
            makeStudyWithStudyDate(book, StudyStatus.IN_PROGRESS,
                startDate, endDate));

        studyRepository.save(makeStudyWithStudyDate(book, StudyStatus.RECRUITING_FINISHED,
            LocalDate.of(2022, 8, 10), LocalDate.of(2022, 10, 20)));
        studyRepository.save(makeStudyWithStudyDate(book, StudyStatus.IN_PROGRESS,
            LocalDate.of(2022, 8, 10), LocalDate.of(2022, 10, 20)));
        studyRepository.save(
            makeStudyWithStudyDate(book, StudyStatus.IN_PROGRESS, startDate, today));
    }

    @AfterEach
    void tearDown() {
        studyRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("상태 업데이트 테스트")
    class UpdateStudyWithMembersTest {

        @Test
        @DisplayName("S 스터디 진행 날짜가 지난 '구인 중' 또는 '구인 완료' 인 스터디 상태를 '진행 중' 으로 변경한다 ")
        void changeToInProgress() {
            service.updateStudyWithMembers(recruitingStudy.getId(), StudyStatus.IN_PROGRESS,
                StudyMemberStatus.DENIED);

            Study foundStudy = studyRepository.findById(recruitingStudy.getId()).get();

            Assertions.assertThat(foundStudy.getStatus())
                .isEqualTo(StudyStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("S 스터디 진행완료 날짜가 지난 스터디들을 '진행 완료'상태로 변경한다 ")
        void changeToFinished() {
            service.updateStudy(toBeFinishedStudy.getId(), StudyStatus.FINISHED);

            Study foundStudy = studyRepository.findById(toBeFinishedStudy.getId()).get();

            Assertions.assertThat(foundStudy.getStatus())
                .isEqualTo(StudyStatus.FINISHED);
        }
    }

    @Nested
    @DisplayName("업데이트 대상 스터디 가져오기 테스트")
    class GetAllStudiesTest {

        @Test
        @DisplayName("S '진행중' 으로 변경할 모든 스터디들을 가져온다 ")
        void getAll() {
            ExpiredStudies studies = service.getAllStudiesToBeProcessed(StudyStatus.IN_PROGRESS);

            Assertions.assertThat(studies.studies())
                .hasSize(2);
        }

        @Test
        @DisplayName("S '진행완료' 로 변경할 모든 스터디들을 가져온다 ")
        void getAllToBeFinished() {
            ExpiredStudies studies = service.getAllStudiesToBeProcessed(StudyStatus.FINISHED);

            Assertions.assertThat(studies.studies())
                .hasSize(1);
        }
    }
}