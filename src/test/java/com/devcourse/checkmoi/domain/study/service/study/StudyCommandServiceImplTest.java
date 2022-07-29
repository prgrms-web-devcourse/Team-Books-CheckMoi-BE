package com.devcourse.checkmoi.domain.study.service.study;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.CreateStudy;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
import java.time.LocalDate;
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

    @Nested
    @DisplayName("스터디 등록 #5")
    class CreateStudy {

        @Test
        @DisplayName("S 스터디를 등록할 수 있다")
        void createStudy() {
            StudyRequest.CreateStudy request = StudyRequest.CreateStudy.builder()
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

            when(studyConverter.createToEntity(any(StudyRequest.CreateStudy.class)))
                .thenReturn(study);
            when(studyRepository.save(any(Study.class)))
                .thenReturn(study);
            Long got = studyCommandService.createStudy(request);

            assertThat(got).isEqualTo(want);
        }
    }
}