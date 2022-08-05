package com.devcourse.checkmoi.domain.study.service;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyServiceValidator;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.global.model.PageRequest;
import java.util.List;
import org.assertj.core.api.Assertions;
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

    @Nested
    @DisplayName("특정 책에 대한 스터디 목록 조회 #43")
    class GetStudiesTest {

        @Test
        @DisplayName("현재 모집중인 특정 책에 대한 스터디 목록 조회")
        void getStudies() {
            Long bookId = 1L;
            PageRequest pageRequest = new PageRequest();
            Pageable pageable = pageRequest.of();
            Page<Study> studies = new PageImpl<>(
                List.of(
                    makeStudyWithId(makeBookWithId(1L), StudyStatus.RECRUITING, 1L),
                    makeStudyWithId(makeBookWithId(1L), StudyStatus.RECRUITING, 3L)
                )
            );
            Page<StudyInfo> studyInfos = studies.map(
                study -> StudyInfo.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .thumbnailUrl(study.getThumbnailUrl())
                    .description(study.getDescription())
                    .currentParticipant(study.getCurrentParticipant())
                    .maxParticipant(study.getMaxParticipant())
                    .gatherStartDate(study.getGatherStartDate())
                    .gatherEndDate(study.getGatherEndDate())
                    .studyStartDate(study.getStudyStartDate())
                    .studyEndDate(study.getStudyEndDate())
                    .build()
            );
            Studies want = Studies.builder()
                .studies(studyInfos)
                .build();
            given(studyRepository.findRecruitingStudyByBookId(anyLong(), any(Pageable.class)))
                .willReturn(studies);
            given(studyConverter.studyToStudyInfo(studies.getContent().get(0))).willReturn(
                studyInfos.getContent().get(0));
            given(studyConverter.studyToStudyInfo(studies.getContent().get(1))).willReturn(
                studyInfos.getContent().get(1));

            Studies got = studyQueryService.getStudies(bookId, pageable);

            assertThat(got).usingRecursiveComparison().isEqualTo(want);
        }
    }

    @Nested
    @DisplayName("스터디 신청 내역 조회")
    public class GetStudyAppliersTest {

        private Long studyLeaderId = 1L;

        private Long studyId = 1L;

        private StudyAppliers expectedAppliers = createAppliersData();


        @Test
        @DisplayName("S 해당 스터디의 스터디 장은 스터디 신청 내역을 조회한다")
        void getAsLeaderSuccess() {
            Long userId = 1L;

            given(studyRepository.findStudyOwner(studyId))
                .willReturn(studyLeaderId);

            given(studyRepository.getStudyAppliers(studyId))
                .willReturn(expectedAppliers);

            doNothing()
                .when(studyValidator)
                .validateStudyOwner(anyLong(), anyLong(), anyString());

            StudyAppliers returnedAppliers = studyQueryService.getStudyAppliers(userId, studyId);

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

        private StudyAppliers createAppliersData() {

            return StudyAppliers.builder()
                .appliers(
                    List.of(
                        UserInfo.builder()
                            .id(1L)
                            .email("abc@naver.com")
                            .name("abc")
                            .profileImageUrl("https://south/dev/abc.png")
                            .temperature(36.5f)
                            .build(),
                        UserInfo.builder()
                            .id(2L)
                            .email("abcd@naver.com")
                            .name("abcd")
                            .profileImageUrl("https://south/dev/abcd.png")
                            .temperature(36.5f)
                            .build(),
                        UserInfo.builder()
                            .id(3L)
                            .email("abce@naver.com")
                            .name("abce")
                            .profileImageUrl("https://south/dev/abce.png")
                            .temperature(36.5f)
                            .build()
                    )
                ).build();
        }
    }
}

