package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.lang.Nullable;

public sealed interface StudyRequest permits Create, Edit, Audit, Search {

    record Search(
        @Nullable Long userId,
        @Nullable Long studyId,
        @Nullable Long bookId,
        @Nullable String memberStatus,
        @Nullable String studyStatus
    ) implements StudyRequest {

        @Builder
        public Search {
        }
    }


    record Create(
        Long bookId,
        String name,
        String thumbnail,
        String description,
        Integer maxParticipant,
        LocalDate gatherStartDate,
        LocalDate gatherEndDate,
        LocalDate studyStartDate,
        LocalDate studyEndDate
    ) implements StudyRequest {

        @Builder
        public Create {
        }
    }

    record Edit(
        String name,
        String thumbnail,
        String description,
        String status
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
