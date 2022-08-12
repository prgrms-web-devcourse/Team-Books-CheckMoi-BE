package com.devcourse.checkmoi.domain.study.service;

import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeStudyInfo;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMemberInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyServiceValidator;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.global.model.SimplePage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class StudyQueryServiceImplTest {

    @InjectMocks
    StudyQueryServiceImpl studyQueryService;

    @Mock
    StudyConverter studyConverter;

    @Mock
    StudyRepository studyRepository;

    @Mock
    StudyServiceValidator studyValidator;

    @Mock
    StudyMemberRepository studyMemberRepository;

    @Nested
    @DisplayName("특정 책에 대한 스터디 목록 조회 #43")
    class GetStudiesTest {

        @Test
        @DisplayName("현재 모집중인 특정 책에 대한 스터디 목록 조회")
        void getStudies() {
            Long bookId = 1L;
            long totalPage = 1L;
            SimplePage simplePage = SimplePage.builder()
                .page(1)
                .size(2)
                .build();
            Pageable pageable = simplePage.pageRequest();
            Page<StudyInfo> studies = new PageImpl<>(List.of(
                makeStudyInfo(makeStudyWithId(makeBookWithId(1L), StudyStatus.RECRUITING, 1L)),
                makeStudyInfo(makeStudyWithId(makeBookWithId(1L), StudyStatus.RECRUITING, 3L))
            ));

            Studies want = Studies.builder()
                .studies(studies.getContent())
                .totalPage(totalPage)
                .build();
            given(studyRepository.findRecruitingStudyByBookId(anyLong(), any(Pageable.class)))
                .willReturn(studies);

            Studies got = studyQueryService.getStudies(bookId, pageable);

            assertThat(got).usingRecursiveComparison().isEqualTo(want);
        }
    }

    @Nested
    @DisplayName("스터디 신청 내역 조회")
    class GetStudyMembersTest {

        private Long studyLeaderId = 1L;

        private Long studyId = 1L;

        private StudyMembers expectedAppliers = createAppliersData();


        @Test
        @DisplayName("S 해당 스터디의 스터디 장은 스터디 신청 내역을 조회한다")
        void getAsLeaderSuccess() {
            Long userId = 1L;

            given(studyRepository.findStudyOwner(studyId))
                .willReturn(studyLeaderId);

            given(studyRepository.getStudyApplicants(studyId))
                .willReturn(expectedAppliers);

            doNothing()
                .when(studyValidator)
                .validateStudyOwner(anyLong(), anyLong(), anyString());

            StudyMembers returnedAppliers = studyQueryService.getStudyAppliers(userId, studyId);

            Assertions.assertThat(returnedAppliers)
                .usingRecursiveComparison()
                .isEqualTo(expectedAppliers);
        }

        @Test
        @DisplayName("S 해당 스터디의 스터디 장이 아니면 스터디 신청 내역 조회에 실패한다")
        void getAsNonLeaderFail() {
            Long userId = 2L;

            given(studyRepository.findStudyOwner(studyId))
                .willReturn(studyLeaderId);

            doThrow(NotStudyOwnerException.class)
                .when(studyValidator)
                .validateStudyOwner(anyLong(), anyLong(), anyString());

            Assertions.assertThatThrownBy(() ->
                studyQueryService.getStudyAppliers(userId, studyId)
            ).isInstanceOf(NotStudyOwnerException.class);
        }

        private StudyMembers createAppliersData() {
            List<StudyUserInfo> users = List.of(
                makeStudyUserInfoWithId(1L),
                makeStudyUserInfoWithId(2L),
                makeStudyUserInfoWithId(3L)
            );

            return StudyMembers.builder()
                .members(
                    List.of(
                        StudyMemberInfo.builder()
                            .id(1L)
                            .user(makeStudyUserInfoWithId(1L))
                            .build(),
                        StudyMemberInfo.builder()
                            .id(1L)
                            .user(makeStudyUserInfoWithId(1L))
                            .build(),
                        StudyMemberInfo.builder()
                            .id(1L)
                            .user(makeStudyUserInfoWithId(1L))
                            .build()
                    )
                )
                .build();
        }

        private StudyUserInfo makeStudyUserInfoWithId(Long id) {
            String email = UUID.randomUUID().toString().substring(20);
            return StudyUserInfo.builder()
                .id(id)
                .email(email + "@naver.com")
                .name("abc")
                .image("https://south/dev/abc.png")
                .temperature(36.5f)
                .build();
        }
    }

    @Nested
    @DisplayName("내 스터디 참여 현황 조회 #116")
    class MyStudiesTest {

        Study study1;

        Study study2;

        Study study3;

        @BeforeEach
        void setUp() {
            Book book = makeBookWithId(1L);
            study1 = makeStudyWithId(book, StudyStatus.IN_PROGRESS, 1L);
            study2 = makeStudyWithId(book, StudyStatus.FINISHED, 2L);
            study3 = makeStudyWithId(book, StudyStatus.RECRUITING, 3L);
        }

        @Test
        @DisplayName("S 내가 참여한 스터디 목록을 조회한다.")
        void getParticipationStudies() {
            Long userId = 1L;
            Studies participation = new Studies(
                List.of(makeStudyInfo(study1)),
                1
            );

            given(studyRepository.getParticipationStudies(anyLong()))
                .willReturn(participation);

            Studies got = studyQueryService.getParticipationStudies(userId);

            assertThat(got).usingRecursiveComparison().isEqualTo(participation);

        }

        @Test
        @DisplayName("S 내가 참여한 종료된 스터디 목록을 조회한다.")
        void getFinishedStudies() {
            Long userId = 1L;
            Studies finished = new Studies(
                List.of(makeStudyInfo(study2)),
                0
            );

            given(studyRepository.getFinishedStudies(anyLong()))
                .willReturn(finished);

            Studies got = studyQueryService.getFinishedStudies(userId);

            assertThat(got).usingRecursiveComparison().isEqualTo(finished);

        }

        @Test
        @DisplayName("S 내가 스터디장인 스터디 목록을 조회한다")
        void getOwnedStudies() {
            Long userId = 1L;
            Studies owned = new Studies(
                List.of(makeStudyInfo(study3)),
                0
            );

            given(studyRepository.getOwnedStudies(anyLong()))
                .willReturn(owned);

            Studies got = studyQueryService.getOwnedStudies(userId);

            assertThat(got).usingRecursiveComparison().isEqualTo(owned);
        }
    }

    @Nested
    @DisplayName("스터디가 진행중인 확인 #129")
    class OngoingStudyTest {

        private final Book book = makeBookWithId(1L);


        @Test
        @DisplayName("S 스터디가 진행중이라면 아무것도 반환하지 않음")
        void ongoingStudy() {
            Study study = makeStudyWithId(book, StudyStatus.RECRUITING, 1L);

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.of(study));

            studyQueryService.validateOngoingStudy(study.getId());

            then(studyValidator)
                .should(times(1))
                .validateOngoingStudy(any(Study.class));
        }

        @Test
        @DisplayName("F 스터디가 종료되었다면 예외 발생")
        void finishedStudy() {
            Study finishedStudy = makeStudyWithId(book, StudyStatus.FINISHED, 1L);

            given(studyRepository.findById(anyLong()))
                .willReturn(Optional.of(finishedStudy));
            doThrow(FinishedStudyException.class)
                .when(studyValidator).validateOngoingStudy(finishedStudy);

            assertThatExceptionOfType(FinishedStudyException.class)
                .isThrownBy(() -> studyQueryService.validateOngoingStudy(finishedStudy.getId()));
        }
    }

    @Nested
    @DisplayName("해당 유저가 스터디에 참여중인지 확인 #129")
    class ParticipateUserTest {

        private final Book book = makeBookWithId(1L);

        private final Study study = makeStudyWithId(book, StudyStatus.RECRUITING, 1L);

        private final User user = makeUserWithId(1L);

        @Test
        @DisplayName("현재 유저가 해당 스터디에 참여중일경우 아무 동작하지 않음")
        void participateUser() {
            Long memberId = 1L;

            given(studyMemberRepository.participateUserInStudy(anyLong(), anyLong()))
                .willReturn(memberId);

            studyQueryService.validateParticipateUser(study.getId(), user.getId());

            then(studyValidator)
                .should(times(1))
                .validateParticipateUser(anyLong());
        }

        @Test
        @DisplayName("현재 유저가 해당 스터디에 참여하지 않을 경우 예외 발생")
        void notParticipateUser() {
            Long otherUserId = 2L;
            Long notFoundMemberId = null;

            given(studyMemberRepository.participateUserInStudy(anyLong(), anyLong()))
                .willReturn(notFoundMemberId);
            doThrow(NotJoinedMemberException.class)
                .when(studyValidator).validateParticipateUser(notFoundMemberId);

            assertThatExceptionOfType(NotJoinedMemberException.class)
                .isThrownBy(
                    () -> studyQueryService.validateParticipateUser(study.getId(), otherUserId));
        }
    }
}

