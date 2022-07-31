package com.devcourse.checkmoi.domain.study.service.study;

import static com.devcourse.checkmoi.global.exception.ErrorMessage.ACCESS_DENIED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinRequestNotFoundException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.study.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
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

    @Nested
    @DisplayName("스터디 등록 #5")
    class Create {

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
            Long want = 1L;

            when(studyConverter.createToEntity(any(StudyRequest.Create.class)))
                .thenReturn(study);
            when(studyRepository.save(any(Study.class)))
                .thenReturn(study);
            Long got = studyCommandService.createStudy(request);

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
                .build();
            Long userId = 1L;
            Long studyId = 1L;
            Study study = Study.builder()
                .id(1L)
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
    class Audit {

        @Test
        @DisplayName("S 스터디 가입 승낙 및 거절할 수 있다.")
        void auditStudyParticipationTest() {
            Long studyId = 1L;
            Long memberId = 1L;
            Long userId = 1L;
            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();
            Long studyOwnerId = 1L;
            StudyMember studyMember = StudyMember.builder()
                .id(1L)
                .status(StudyMemberStatus.PENDING)
                .build();
            given(studyRepository.existsById(anyLong())).willReturn(true);
            given(studyRepository.findStudyOwner(anyLong())).willReturn(studyOwnerId);
            given(studyMemberRepository.findById(anyLong())).willReturn(
                Optional.of(studyMember));

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
            StudyMember studyMember = StudyMember.builder()
                .id(1L)
                .status(StudyMemberStatus.PENDING)
                .build();
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
            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();
            Long studyOwnerId = 2L;

            StudyMember studyMember = StudyMember.builder()
                .id(1L)
                .status(StudyMemberStatus.PENDING)
                .build();
            given(studyRepository.existsById(anyLong())).willReturn(true);
            given(studyRepository.findStudyOwner(anyLong())).willReturn(studyOwnerId);

            assertThatExceptionOfType(NotStudyOwnerException.class)
                .isThrownBy(
                    () -> studyCommandService.auditStudyParticipation(studyId, memberId, userId,
                        request))
                .withMessage("스터디 승인 권한이 없습니다. 유저 Id : " + userId + " 스터디 장 Id : " + studyOwnerId);
        }

        @Test
        @DisplayName("F 해당 신청이 존재하지 않을 경우 예외가 발생한다.")
        void studyJoinRequestNotFound() {
            Long studyId = 1L;
            Long memberId = 1L;
            Long userId = 1L;
            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();
            Long studyOwnerId = 1L;
            StudyMember studyMember = StudyMember.builder()
                .id(1L)
                .status(StudyMemberStatus.PENDING)
                .build();
            given(studyRepository.existsById(anyLong())).willReturn(true);
            given(studyRepository.findStudyOwner(anyLong())).willReturn(studyOwnerId);
            given(studyMemberRepository.findById(anyLong())).willReturn(
                Optional.empty());

            assertThatExceptionOfType(StudyJoinRequestNotFoundException.class)
                .isThrownBy(
                    () -> studyCommandService.auditStudyParticipation(studyId, memberId, userId,
                        request));
        }
    }
}