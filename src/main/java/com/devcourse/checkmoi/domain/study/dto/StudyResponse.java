package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

public sealed interface StudyResponse permits StudyInfo, Studies {

    record StudyInfo(
        Long id,
        String name,
        String thumbnailUrl,
        String description,
        int currentParticipant,
        Integer maxParticipant,
        LocalDate gatherStartDate,
        LocalDate gatherEndDate,
        LocalDate studyStartDate,
        LocalDate studyEndDate
    ) implements StudyResponse {

        @Builder
        public StudyInfo {

        }
    }

    record Studies(
        Page<StudyInfo> studies
    ) implements StudyResponse {

        @Builder
        public Studies {

        }
    }

}
