package com.devcourse.checkmoi.domain.study.dto;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.global.annotation.ValueOfEnum;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;
import org.springframework.lang.Nullable;

public sealed interface StudyRequest permits Create, Edit, Audit, Search {

    record Search(
        @Nullable Long userId,
        @Nullable Long studyId,
        @Nullable Long bookId,
        @Nullable Boolean isMember,
        @Nullable @ValueOfEnum(enumClass = StudyMemberStatus.class) String memberStatus,
        @Nullable @ValueOfEnum(enumClass = StudyStatus.class) String studyStatus
    ) implements StudyRequest {

        @Builder
        public Search {
        }
    }


    record Create(
        Long bookId,
        @Size(max = 20, message = "스터디 이름은 20자를 넘길 수 없습니다.")
        String name,
        @URL(message = "올바른 이미지 URL이 필요합니다.")
        String thumbnail,
        @Size(max = 500, message = "스터디 설명은 500자를 넘길 수 없습니다.")
        String description,
        @Min(value = 1, message = "최대 참여 인원은 1명 이상이어야 합니다.")
        @Max(value = 10, message = "최대 참여 인원이 10명 이상일 수 없습니다.")
        Integer maxParticipant,
        LocalDate gatherStartDate,
        @FutureOrPresent(message = "모집 종료일자가 현재보다 과거일 수 없습니다.")
        LocalDate gatherEndDate,
        @FutureOrPresent(message = "스터디 시작일자가 현재보다 과거일 수 없습니다.")
        LocalDate studyStartDate,
        @FutureOrPresent(message = "스터디 종료일자가 현재보다 과거일 수 없습니다.")
        LocalDate studyEndDate
    ) implements StudyRequest {

        @Builder
        public Create {
        }
    }

    record Edit(
        @Size(max = 20, message = "스터디 이름은 20자를 넘길 수 없습니다.")
        String name,
        @URL(message = "올바른 이미지 URL이 필요합니다.")
        String thumbnail,
        @Size(max = 500, message = "스터디 설명은 500자를 넘길 수 없습니다.")
        String description,
        String status
    ) implements StudyRequest {

        @Builder
        public Edit {
        }
    }

    record Audit(
        @ValueOfEnum(enumClass = StudyMemberStatus.class) String status
    ) implements StudyRequest {

        @Builder
        public Audit {

        }
    }
}
