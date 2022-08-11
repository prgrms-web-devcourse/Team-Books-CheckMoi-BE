package com.devcourse.checkmoi.domain.study.service;

import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.ACCESS_DENIED;
import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.STUDY_JOIN_REQUEST_DUPLICATE;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMemberWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.exception.DuplicateStudyJoinRequestException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinRequestNotFoundException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyServiceValidator;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyCommandServiceImplTest {

    @InjectMocks
    StudyCommandServiceImpl studyCommandService;

    @Mock
    StudyConverter studyConverter;

    @Mock
    StudyRepository studyRepository;

    @Mock
    StudyMemberRepository studyMemberRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StudyServiceValidator studyValidator;

    @Nested
    @DisplayName("스터디 등록 #5")
    class CreateTest {

        @Test
        @DisplayName("S 스터디를 등록할 수 있다")
        void createStudy() {
            StudyRequest.Create request = StudyRequest.Create.builder()
                .bookId(1L)
                .name("스터디 이름")
                .thumbnail("스터디 썸네일 URL")
                .description("스터디입니다")
                .maxParticipant(5)
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .build();
            Long userId = 1L;
            User user = makeUserWithId(1L);
            Study study = Study.builder()
                .book(
                    Book.builder().
                        id(request.bookId())
                        .build()
                )
                .id(1L)
                .name(request.name())
                .thumbnailUrl(request.thumbnail())
                .description(request.description())
                .maxParticipant(request.maxParticipant())
                .gatherStartDate(request.gatherStartDate())
                .gatherEndDate(request.gatherEndDate())
                .build();
            StudyMember studyMember = StudyMember.builder()
                .id(1L)
                .status(OWNED)
                .user(makeUserWithId(1L))
                .study(makeStudyWithId(makeBookWithId(1L), RECRUITING, 1L))
                .build();
            Long want = 1L;

            when(studyConverter.createToEntity(any(StudyRequest.Create.class)))
                .thenReturn(study);
            when(studyRepository.save(any(Study.class)))
                .thenReturn(study);
            when(studyMemberRepository.save(any(StudyMember.class)))
                .thenReturn(studyMember);
            Long got = studyCommandService.createStudy(request, userId);

            assertThat(got).isEqualTo(want);
        }
    }

    @Nested
    @DisplayName("스터디 수정 #30")
    class EditTest {

        @Test
        @DisplayName("S 스터디 정보 수정을 할 수 있다")
        void editStudyInfo() {
            StudyRequest.Edit request = StudyRequest.Edit.builder()
                .name("스터디 이름")
                .thumbnail("https://example.com")
                .description("스터디 설명")
                .status(IN_PROGRESS.toString())
                .build();
            Long userId = 1L;
            Long studyId = 1L;
            Study study = Study.builder()
                .id(1L)
                .status(RECRUITING)
                .build();
            when(studyRepository.findStudyOwner(anyLong()))
                .thenReturn(userId);
            when(studyRepository.findById(studyId))
                .thenReturn(Optional.ofNullable(study));

            Long got = studyCommandService.editStudyInfo(studyId, userId, request);

            assertThat(got).isEqualTo(studyId);
        }

        @Test
        @DisplayName("F 스터디 정보 수정 중 로그인 유저가 스터디 장이 아니라면 예외가 발생합니다")
        void validateStudyOwner() {
            StudyRequest.Edit request = StudyRequest.Edit.builder()
                .name("스터디 이름")
                .thumbnail("https://example.com")
                .description("스터디 설명")
                .build();
            Long userId = 1L;
            Long studyOwnerId = 2L;
            Long studyId = 1L;
            Study study = Study.builder()
                .id(1L)
                .build();

            when(studyRepository.findStudyOwner(anyLong()))
                .thenThrow(new NotStudyOwnerException(
                    "스터디 정보 수정 권한이 없습니다. 유저 아이디 : " + userId + " 스터디장 Id : " + studyOwnerId,
                    ACCESS_DENIED));

            assertThatExceptionOfType(NotStudyOwnerException.class)
                .isThrownBy(() -> studyCommandService.editStudyInfo(studyId, userId, request))
                .withMessage(
                    "스터디 정보 수정 권한이 없습니다. 유저 아이디 : " + userId + " 스터디장 Id : " + studyOwnerId);
        }

        @Test
        @DisplayName("F 스터디 정보 수정 중 해당 스터디 ID를 가진 스터디가 존재하지 않을 경우 예외가 발생합니다.")
        void studyNotFound() {
            StudyRequest.Edit request = StudyRequest.Edit.builder()
                .name("스터디 이름")
                .thumbnail("https://example.com")
                .description("스터디 설명")
                .build();
            Long userId = 1L;
            Long studyId = 1L;
            Study study = Study.builder()
                .id(1L)
                .build();
            when(studyRepository.findStudyOwner(anyLong()))
                .thenReturn(userId);
            when(studyRepository.findById(studyId))
                .thenThrow(new StudyNotFoundException());

            assertThatExceptionOfType(StudyNotFoundException.class)
                .isThrownBy(() -> studyCommandService.editStudyInfo(studyId, userId, request));
        }
    }

    @Nested
    @DisplayName("스터디 가입 승낙 및 거절 #37")
    class AuditTest {

        @Test
        @DisplayName("S 스터디 가입 승낙 및 거절할 수 있다.")
        void auditStudyParticipationTest() {
            Long studyId = 1L;
            Long memberId = 1L;
            Long userId = 1L;
            Long studyOwnerId = 1L;

            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();

            StudyMember studyMember = StudyMember.builder()
                .id(1L)
                .status(StudyMemberStatus.PENDING)
                .build();

            given(studyRepository.existsById(anyLong())).willReturn(true);
            given(studyRepository.findStudyOwner(anyLong())).willReturn(studyOwnerId);
            given(studyMemberRepository.findById(anyLong()))
                .willReturn(Optional.of(studyMember));

            studyCommandService.auditStudyParticipation(studyId, memberId, userId, request);

            assertThat(studyMember.getStatus()).isEqualTo(StudyMemberStatus.ACCEPTED);
        }

        @Test
        @DisplayName("F 존재하지 않는 스터디 ID일 경우 예외가 발생한다.")
        void validateExistStudy() {
            Long studyId = 1L;
            Long memberId = 1L;
            Long userId = 1L;
            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();

            doThrow(StudyNotFoundException.class)
                .when(studyValidator)
                .validateExistStudy(anyBoolean());

            given(studyRepository.existsById(anyLong())).willReturn(false);

            assertThatExceptionOfType(StudyNotFoundException.class)
                .isThrownBy(
                    () -> studyCommandService.auditStudyParticipation(studyId, memberId, userId,
                        request));
        }

        @Test
        @DisplayName("F 스터디장이 아닌 유저가 변경 시도시 예외가 발생한다.")
        void validateStudyOwner() {
            Long studyId = 1L;
            Long memberId = 1L;
            Long userId = 1L;
            Long studyOwnerId = 2L;

            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();

            String errorMessage =
                "스터디 승인 권한이 없습니다. 유저 Id : " + userId + " 스터디 장 Id : " + studyOwnerId;

            doThrow(new NotStudyOwnerException(errorMessage, ErrorMessage.ACCESS_DENIED))
                .when(studyValidator)
                .validateStudyOwner(anyLong(), anyLong(), anyString());

            given(studyRepository.existsById(anyLong())).willReturn(true);
            given(studyRepository.findStudyOwner(anyLong())).willReturn(studyOwnerId);

            assertThatExceptionOfType(NotStudyOwnerException.class)
                .isThrownBy(
                    () -> studyCommandService
                        .auditStudyParticipation(studyId, memberId, userId, request))
                .withMessage("스터디 승인 권한이 없습니다. 유저 Id : " + userId + " 스터디 장 Id : " + studyOwnerId);
        }

        @Test
        @DisplayName("F 해당 신청이 존재하지 않을 경우 예외가 발생한다.")
        void studyJoinRequestNotFound() {
            Long studyId = 1L;
            Long memberId = 1L;
            Long userId = 1L;
            Long studyOwnerId = 1L;

            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();

            given(studyRepository.existsById(anyLong())).willReturn(true);
            given(studyRepository.findStudyOwner(anyLong())).willReturn(studyOwnerId);
            given(studyMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            assertThatExceptionOfType(StudyJoinRequestNotFoundException.class)
                .isThrownBy(() ->
                    studyCommandService
                        .auditStudyParticipation(studyId, memberId, userId, request));
        }
    }

    @Nested
    @DisplayName("스터디 가입 신청 #52")
    class RequestStudyJoinTest {


        @Test
        @DisplayName("S 스터디 가입 신청을 할 수 있습니다.")
        void requestStudyJoin() {

            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), RECRUITING, 1L);
            StudyMember studyMember = makeStudyMember(study, user, StudyMemberStatus.PENDING);

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.of(study));
            given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
            given(studyMemberRepository.findByUserAndStudy(anyLong(), anyLong()))
                .willReturn(Optional.empty());
            given(studyMemberRepository.save(any(StudyMember.class)))
                .willReturn(studyMember);

            Long got = studyCommandService.requestStudyJoin(study.getId(), user.getId());
            Long want = studyMember.getId();

            assertThat(got).isEqualTo(want);
        }

        @Test
        @DisplayName("S 만약 거절당했다면 스터디 가입 재신청을 할 수 있습니다.")
        void reRequestStudyJoin() {
            Study study = makeStudyWithId(makeBook(), RECRUITING, 1L);
            User user = makeUserWithId(1L);
            StudyMember studyMember =
                makeStudyMemberWithId(study, user, StudyMemberStatus.DENIED, 1L);

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.of(study));
            given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
            given(studyMemberRepository.findByUserAndStudy(anyLong(), anyLong()))
                .willReturn(Optional.of(studyMember));
            given(studyMemberRepository.save(any(StudyMember.class)))
                .willReturn(studyMember);

            doNothing()
                .when(studyValidator)
                .validateDuplicateStudyMemberRequest(any(StudyMember.class));

            Long got = studyCommandService.requestStudyJoin(study.getId(), user.getId());
            Long want = studyMember.getId();

            assertThat(got).isEqualTo(want);
        }


        @Test
        @DisplayName("F 해당 스터디가 존재하지 않는다면 예외 발생")
        void studyNotFound() {
            Long studyId = 1L;
            Long userId = 1L;

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            assertThatExceptionOfType(StudyNotFoundException.class)
                .isThrownBy(() -> studyCommandService.requestStudyJoin(studyId, userId));
        }

        @Test
        @DisplayName("F 유저가 존재하지 않는다면 예외 발생")
        void userNotFound() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), RECRUITING, 1L);

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.of(study));
            given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(
                    () -> studyCommandService.requestStudyJoin(study.getId(), user.getId()));
        }

        @Test
        @DisplayName("F 유저가 이미 가입 요청을 했다면 예외 발생")
        void duplicateStudyJoin() {
            Study study = makeStudyWithId(makeBookWithId(1L), RECRUITING, 1L);
            User user = makeUserWithId(1L);

            StudyMember studyMember =
                makeStudyMemberWithId(study, user, OWNED, 1L);

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.of(study));
            given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
            given(studyMemberRepository.findByUserAndStudy(anyLong(), anyLong()))
                .willReturn(Optional.of(studyMember));

            doThrow(new DuplicateStudyJoinRequestException(STUDY_JOIN_REQUEST_DUPLICATE))
                .when(studyValidator)
                .validateDuplicateStudyMemberRequest(any(StudyMember.class));

            assertThatExceptionOfType(DuplicateStudyJoinRequestException.class)
                .isThrownBy(
                    () -> studyCommandService.requestStudyJoin(study.getId(), user.getId()));
        }

    }
}
