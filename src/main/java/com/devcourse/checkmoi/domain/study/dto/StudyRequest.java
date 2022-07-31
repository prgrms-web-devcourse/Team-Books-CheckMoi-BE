package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import java.time.LocalDate;
import lombok.Builder;

public sealed interface StudyRequest permits Create, Edit, Audit {

    record Create(
        Long bookId,
        String name,
        String thumbnail,
        String description,
        Integer maxParticipant,
        LocalDate gatherStartDate,
        LocalDate gatherEndDate
    ) implements StudyRequest {

        @Builder
        public Create {
        }
    }

    record Edit(
        String name,
        String thumbnail,
        String description
    ) implements StudyRequest {

        @Builder
        public Edit {
        }
    }

    record Audit(
        String status
    ) implements StudyRequest {

        @Builder
        public Audit {

        }
    }
}
