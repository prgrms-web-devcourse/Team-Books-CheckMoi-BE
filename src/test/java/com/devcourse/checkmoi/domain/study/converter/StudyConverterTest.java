package com.devcourse.checkmoi.domain.study.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudyConverterTest {

    private final StudyConverter studyConverter = new StudyConverter();

    @Nested
    @DisplayName("스터디 등록 (개설) #5")
    class Create {

        @Test
        @DisplayName("S 스터디 등록 요청을 엔티티로 변환할 수 있다.")
        void createToEntityTest() {
            StudyRequest.Create request = StudyRequest.Create.builder()
                .bookId(1L)
                .name("스터디 이름")
                .thumbnail("스터디 썸네일 URL")
                .description("스터디입니다")
                .maxParticipant(5)
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build();
            Study want = Study.builder()
                .book(
                    Book.builder().
                        id(request.bookId())
                        .build()
                )
                .name(request.name())
                .thumbnailUrl(request.thumbnail())
                .description(request.description())
                .maxParticipant(request.maxParticipant())
                .gatherStartDate(request.gatherStartDate())
                .gatherEndDate(request.gatherEndDate())
                .studyStartDate(request.studyStartDate())
                .studyEndDate(request.studyEndDate())
                .status(StudyStatus.RECRUITING)
                .build();

            Study got = studyConverter.createToEntity(request);

            assertThat(got)
                .usingRecursiveComparison()
                .isEqualTo(want);
        }
    }
}