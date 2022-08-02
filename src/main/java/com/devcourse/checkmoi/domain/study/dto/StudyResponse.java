package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

public sealed interface StudyResponse permits
    StudyInfo, StudyDetailWithMembers, Studies, StudyDetailInfo, StudyBookInfo {

    record StudyInfo(
        Long id,
        String name,
        String thumbnailUrl,
        String description,
        int currentParticipant,
        Integer maxParticipant,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate gatherStartDate,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate gatherEndDate,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate studyStartDate,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate studyEndDate
    ) implements StudyResponse {

        @Builder
        public StudyInfo {

        }
    }

    record StudyDetailInfo(
        Long id,
        String name,
        String status,
        String thumbnailUrl,
        String description,
        Integer currentParticipant,
        Integer maxParticipant,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate gatherStartDate,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate gatherEndDate,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate studyStartDate,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate studyEndDate,

        StudyBookInfo book
    ) implements StudyResponse {

        @Builder
        public StudyDetailInfo {

        }
    }

    record StudyBookInfo(
        Long bookId,
        String title,
        String author,
        String publisher,
        String thumbnail
    ) implements StudyResponse {

        @Builder
        public StudyBookInfo {

        }
    }

    record StudyDetailWithMembers(
        StudyDetailInfo study,
        List<UserInfo> members
    ) implements StudyResponse {

        @Builder
        public StudyDetailWithMembers {

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
