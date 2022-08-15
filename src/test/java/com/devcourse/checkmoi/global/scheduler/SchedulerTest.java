package com.devcourse.checkmoi.global.scheduler;

import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.PENDING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING_FINISHED;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithStudyDate;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMemberInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class SchedulerTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private Scheduler scheduler;

    private User owner;

    private User acceptedUser;

    private Book book;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(makeUser());
        acceptedUser = userRepository.save(makeUser());

        book = bookRepository.save(makeBook());
    }

    @AfterEach
    void tearDown() {
        studyMemberRepository.deleteAllInBatch();
        studyRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("S 스터디 진행날짜가 도래한 스터디의 상태를 진행중으로 변경하며 해당 스터디에 대해 PENDING 상태던 스터디들을 DENIED 로 변경한다")
    @Transactional
    void denySuccess() {
        User pendingUser = userRepository.save(makeUser());
        Study study = studyRepository.save(makeStudyWithStudyDate(book, RECRUITING_FINISHED,
            LocalDate.now(), LocalDate.now().plusDays(2)));

        studyMemberRepository.save(makeStudyMember(study, pendingUser, PENDING));
        studyMemberRepository.save(makeStudyMember(study, owner, OWNED));
        studyMemberRepository.save(makeStudyMember(study, acceptedUser, ACCEPTED));

        scheduler.changeStudyAsInProgress();

        List<StudyMemberInfo> appliers = studyRepository
            .getStudyApplicants(study.getId())
            .members();

        Assertions.assertThat(appliers)
            .isEmpty();
    }


    @Test
    @DisplayName("S 스터디 종료날짜가 도래한 스터디의 상태를 진행 완료로 변경한다")
    void updateSuccess() {
        Study study = studyRepository.save(makeStudyWithStudyDate(book, IN_PROGRESS,
            LocalDate.now().minusDays(2), LocalDate.now().minusDays(1)));

        studyMemberRepository.save(makeStudyMember(study, owner, OWNED));
        studyMemberRepository.save(makeStudyMember(study, acceptedUser, ACCEPTED));

        scheduler.changeStudyAsFinished();
        studyRepository.flush();

        Study foundStudy = studyRepository.findById(study.getId()).get();

        Assertions.assertThat(foundStudy.getStatus())
            .isEqualTo(FINISHED);
    }

}