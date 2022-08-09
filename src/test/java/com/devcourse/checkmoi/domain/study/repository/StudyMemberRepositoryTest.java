package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.RepositoryTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class StudyMemberRepositoryTest extends RepositoryTest {

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private BookRepository bookRepository;

    @Nested
    @DisplayName("현재 유저가 스터디에 참가중인지 확인한다. #129")
    class ExistParticipateStudyTest {

        Study study;

        User user;

        StudyMember studyMember;

        @BeforeEach
        void setUp() {
            Book book = bookRepository.save(makeBook());
            user = userRepository.save(makeUser());
            study = studyRepository.save(makeStudy(book, StudyStatus.RECRUITING));
            studyMember = studyMemberRepository.save(
                makeStudyMember(study, user, StudyMemberStatus.OWNED));
        }

        @AfterEach
        void tearDown() {
            studyMemberRepository.deleteAllInBatch();
            studyRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
            bookRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("S 유저가 해당 스터디에 참여하고 있으면 멤버 ID 반환")
        void existsByStudyIdAndUserId() {
            Long got = studyMemberRepository.participateUserInStudy(study.getId(), user.getId());

            assertThat(got).isEqualTo(studyMember.getId());
        }

        @Test
        @DisplayName("S 유저가 해당 스터디에 참여하지 않으면 null 반환")
        void notExistsByStudyIdAndUserId() {
            Long wrongStudyId = 0L;
            Long got = studyMemberRepository.participateUserInStudy(wrongStudyId, user.getId());

            assertThat(got).isNull();
        }
    }

}