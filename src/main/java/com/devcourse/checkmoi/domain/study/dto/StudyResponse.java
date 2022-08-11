package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.ExpiredStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetail;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public sealed interface StudyResponse permits
    StudyInfo, StudyDetail, StudyDetailWithMembers,
    Studies, StudyBookInfo, StudyUserInfo,
    StudyAppliers, MyStudies,
    ExpiredStudies {

    record StudyInfo(
        Long id,
        String name,
        String thumbnail,
        String description,
        StudyStatus status,
        Integer currentParticipant,
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

    record StudyDetail(
        StudyInfo study,
        StudyBookInfo book
    ) implements StudyResponse {

    }

    record StudyDetailWithMembers(
        StudyInfo study,
        StudyBookInfo book,
        List<StudyUserInfo> members
    ) implements StudyResponse {

        @Builder
        public StudyDetailWithMembers {

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
        String image,
        Float temperature
    ) implements StudyResponse {

        @Builder
        public StudyUserInfo {
        }
    }


    record Studies(
        List<StudyInfo> studies,
        long totalPage
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
        Studies participation,
        Studies owned,
        Studies finished
    ) implements StudyResponse {

        @Builder
        public MyStudies {

        }
    }

    record ExpiredStudies(
        List<Long> studies
    ) implements StudyResponse {

    }
}
