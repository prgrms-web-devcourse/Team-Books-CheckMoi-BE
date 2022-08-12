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
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.exception.DuplicateStudyJoinRequestException;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.exception.NotRecruitingStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyMemberFullException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudyValidatorImplTest {

    StudyValidator studyValidator = new StudyValidatorImpl();

    @Test
    @DisplayName("해당하는 스터디가 존재하는지 검사")
    void validateExistStudy() {
        studyValidator.validateExistStudy(true);
        assertThrows(StudyNotFoundException.class,
            () -> studyValidator.validateExistStudy(false));
    }

    @Test
    @DisplayName("해당하는 아이디는 스터디장의 아이디와 일치하는지 검사")
    void validateStudyOwner() {
        studyValidator.validateStudyOwner(1L, 1L, "message");
        assertThrows(NotStudyOwnerException.class,
            () -> studyValidator.validateStudyOwner(1L, 2L, "스터디장은 2L"));
    }

    @Test
    @DisplayName("스터디에 중복해서 가입신청하는지 검사")
    void validateDuplicateStudyMemberRequest() {
        User user = makeUser();
        studyValidator.validateDuplicateStudyMemberRequest(
            makeStudyMember(makeStudy(makeBook(), RECRUITING), user, DENIED));

        assertThrows(DuplicateStudyJoinRequestException.class,
            () -> studyValidator.validateDuplicateStudyMemberRequest(
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
                .isThrownBy(() -> studyValidator.validateOngoingStudy(study));
        }
    }

    @Nested
    @DisplayName("스터디 멤버 아이디를 확인하여 스터디 참여중인지 검사 #129")
    class ParticipateUserTest {

        @Test
        @DisplayName("스터디 멤버 아이디가 존재한다면 정상 종료")
        void participateUser() {
            Long memberId = 1L;

            studyValidator.validateParticipateUser(memberId);
        }

        @Test
        @DisplayName("스터디 멤버 아이디가 null이라면 예외 발생")
        void notParticipateUser() {
            Long notFoundMemberId = null;
            assertThatExceptionOfType(NotJoinedMemberException.class)
                .isThrownBy(() -> studyValidator.validateParticipateUser(notFoundMemberId));
        }
    }

    @Nested
    @DisplayName("스터디 멤버 아이디를 확인하여 스터디 참여중인지 검사 #200")
    class RecruitingStudyTest {

        Book book = makeBookWithId(1L);

        @Test
        @DisplayName("스터디 상태가 RECRUITING이라면 정상 종료")
        void validateRecruitingStudy() {
            Study study = makeStudyWithId(book, RECRUITING, 1L);

            studyValidator.validateRecruitingStudy(study);
        }

        @Test
        @DisplayName("스터디 상태가 RECRUITING이 아니라면 예외 발생")
        void notRecruitingStudy() {
            Study notRecruitingStudy = makeStudyWithId(book, StudyStatus.IN_PROGRESS, 1L);
            assertThatExceptionOfType(NotRecruitingStudyException.class)
                .isThrownBy(() -> studyValidator.validateRecruitingStudy(notRecruitingStudy));
        }
    }

    @Nested
    @DisplayName("스터디 참가인원이 가득 찼는지 검사 #200")
    class FullMemberStudyTest {

        @Test
        @DisplayName("현재 인원이 최대인원보다 작을 경우 정상 종료")
        void notFullMemberStudy() {
            Study study = Study.builder()
                .currentParticipant(1)
                .maxParticipant(3)
                .build();

            studyValidator.validateFullMemberStudy(study);
        }

        @Test
        @DisplayName("현재 인원이 최대인원에 도달했을 경우 예외 발생")
        void FullMemberStudy() {
            Study memberFullStudy = Study.builder()
                .currentParticipant(3)
                .maxParticipant(3)
                .build();

            assertThatExceptionOfType(StudyMemberFullException.class)
                .isThrownBy(() -> studyValidator.validateFullMemberStudy(memberFullStudy));
        }
    }
}
