package com.devcourse.checkmoi.domain.study.service.study;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
import com.devcourse.checkmoi.domain.study.stub.StudyStub;
import com.devcourse.checkmoi.global.model.PageRequest;
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

    @Nested
    @DisplayName("특정 책에 대한 스터디 목록 조회 #43")
    class GetStudies {

        @Test
        @DisplayName("현재 모집중인 특정 책에 대한 스터디 목록 조회")
        void getStudies() {
            Long bookId = 1L;
            PageRequest pageRequest = new PageRequest();
            Pageable pageable = pageRequest.of();
            Page<Study> studies = new PageImpl<>(
                StudyStub.javaRecrutingStudyStub()
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
}

