package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.CreateStudy;
import java.time.LocalDate;
import lombok.Builder;

public sealed interface StudyRequest permits CreateStudy {

    record CreateStudy(
        Long bookId,
        String name,
        String thumbnail,
        String description,
        Integer maxParticipant,
        LocalDate gatherStartDate,
        LocalDate gatherEndDate
    ) implements StudyRequest {

        @Builder
        public CreateStudy {
        }
    }

}
