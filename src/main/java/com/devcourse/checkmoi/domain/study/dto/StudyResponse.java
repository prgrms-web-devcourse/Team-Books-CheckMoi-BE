package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

public sealed interface StudyResponse permits
    StudyInfo, StudyDetailWithMembers, Studies, StudyDetailInfo, StudyBookInfo, StudyAppliers,
    StudyUserInfo, MyStudies {

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

    record MyStudyInfo(
        Long id,
        String name,
        String category,
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
        LocalDate studyEndDate,
        boolean isOwner

    ) {

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
        Long id,
        String title,
        String image,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDate pubDate,
        String isbn,
        String description,
        @JsonFormat(pattern = "yyyy/MM/dd")
        LocalDateTime createdAt
    ) implements StudyResponse {

        @Builder
        public StudyBookInfo {

        }
    }

    record StudyUserInfo(
        Long id,
        String name,
        String email,
        Float temperature,
        String profileImageUrl
    ) implements StudyResponse {

        @Builder
        public StudyUserInfo {
        }
    }

    record StudyDetailWithMembers(
        StudyDetailInfo study,
        List<StudyUserInfo> members
    ) implements StudyResponse {

        @Builder
        public StudyDetailWithMembers {

        }
    }

    record Studies(
        List<StudyInfo> studies
    ) implements StudyResponse {

        @Builder
        public Studies {

        }
    }

    record StudyAppliers(
        List<StudyUserInfo> appliers
    ) implements StudyResponse {

        @Builder
        public StudyAppliers {
        }
    }

    record MyStudies(
        UserInfo user,
        List<MyStudyInfo> progress,
        List<MyStudyInfo> owned,
        List<MyStudyInfo> finished
    ) implements StudyResponse {

        @Builder
        public MyStudies {

        }
    }
}
