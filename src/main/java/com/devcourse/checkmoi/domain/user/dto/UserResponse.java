package com.devcourse.checkmoi.domain.user.dto;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfoWithStudy;
import com.devcourse.checkmoi.domain.user.dto.UserStudyResponse.StudyInfo;
import java.util.List;
import lombok.Builder;

public sealed interface UserResponse permits UserInfo, UserInfoWithStudy, Register {

    record UserInfo(
        Long id,
        String name,
        String email,
        Float temperature,
        String profileImageUrl
    ) implements UserResponse {

        @Builder
        public UserInfo {
        }
    }

    record UserInfoWithStudy(
        Long id,
        String name,
        String email,
        Float temperature,
        String profileImageUrl,
        List<StudyInfo> studies
    ) implements UserResponse {

        @Builder
        public UserInfoWithStudy {
        }
    }

    record Register(
        Long id,
        String name,
        String email,
        String profileImageUrl,
        String role
    ) implements UserResponse {

        @Builder
        public Register {
        }
    }
}
