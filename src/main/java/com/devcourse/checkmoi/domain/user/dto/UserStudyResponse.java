package com.devcourse.checkmoi.domain.user.dto;

import com.devcourse.checkmoi.domain.user.dto.UserStudyResponse.StudyInfo;
import lombok.Builder;

public sealed interface UserStudyResponse permits StudyInfo {

    record StudyInfo(
        Long id,
        String name,
        String thumbnail
    ) implements UserStudyResponse {

        @Builder
        public StudyInfo {
        }
    }
}
