package com.devcourse.checkmoi.domain.study.service.validator;


import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.DENIED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.PENDING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.exception.DuplicateStudyJoinRequestException;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudyServiceValidatorImplTest {

    StudyServiceValidator studyServiceValidator = new StudyServiceValidatorImpl();

    @Test
    @DisplayName("해당하는 스터디가 존재하는지 검사")
    void validateExistStudy() {
        studyServiceValidator.validateExistStudy(true);
        assertThrows(StudyNotFoundException.class,
            () -> studyServiceValidator.validateExistStudy(false));
    }

    @Test
    @DisplayName("해당하는 아이디는 스터디장의 아이디와 일치하는지 검사")
    void validateStudyOwner() {
        studyServiceValidator.validateStudyOwner(1L, 1L, "message");
        assertThrows(NotStudyOwnerException.class,
            () -> studyServiceValidator.validateStudyOwner(1L, 2L, "스터디장은 2L"));
    }

    @Test
    @DisplayName("스터디에 중복해서 가입신청하는지 검사")
    void validateDuplicateStudyMemberRequest() {
        User user = makeUser();
        studyServiceValidator.validateDuplicateStudyMemberRequest(
            makeStudyMember(makeStudy(makeBook(), RECRUITING), user, DENIED));

        assertThrows(DuplicateStudyJoinRequestException.class,
            () -> studyServiceValidator.validateDuplicateStudyMemberRequest(
                makeStudyMember(makeStudy(makeBook(), RECRUITING), user, PENDING)));
    }

    @Nested
    @DisplayName("스터디가 진행중인지 검사 #129")
    class OngoingStudyTest {

        @Test
        @DisplayName("스터디가 종료되었다면 예외 발생")
        void finishedStudy() {
            Book book = makeBookWithId(1L);
            Study study = makeStudyWithId(book, FINISHED, 1L);

            assertThatExceptionOfType(FinishedStudyException.class)
                .isThrownBy(() -> studyServiceValidator.validateOngoingStudy(study));
        }
    }

    @Nested
    @DisplayName("스터디 멤버 아이디를 확인하여 스터디 참여중인지 검사 #129")
    class ParticipateUserTest {

        @Test
        @DisplayName("스터디 멤버 아이디가 null이라면 예외 발생")
        void participateUser() {
            Long notFoundMemberId = null;
            assertThatExceptionOfType(NotJoinedMemberException.class)
                .isThrownBy(() -> studyServiceValidator.validateParticipateUser(notFoundMemberId));
        }
    }

}
