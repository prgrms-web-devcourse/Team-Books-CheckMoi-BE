package com.devcourse.checkmoi.domain.user.repository;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.Assertions.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.template.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class CustomUserRepositoryImplTest extends RepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyMemberRepository studyMemberRepository;

    @Nested
    @DisplayName("유저의 스터디 개수 조회 #223")
    class UserJoinedStudiesTest {

        User user;

        @BeforeEach
        void setUp() {
            Book book = makeBook();
            bookRepository.save(book);

            user = makeUser();
            userRepository.save(user);

            Study study = makeStudy(book, StudyStatus.RECRUITING);
            studyRepository.save(study);

            StudyMember studyMember = makeStudyMember(study, user, StudyMemberStatus.OWNED);
            studyMemberRepository.save(studyMember);
        }

        @Test
        @DisplayName("S 유저가 현재 참여하고 있는 스터디 개수를 반환한다.")
        void userJoinedStudies() {
            int want = 1;
            int got = userRepository.userJoinedStudies(user.getId());

            assertThat(got).isEqualTo(want);
        }
    }
}