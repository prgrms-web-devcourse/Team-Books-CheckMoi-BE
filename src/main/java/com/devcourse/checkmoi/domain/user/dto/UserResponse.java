package com.devcourse.checkmoi.domain.user.dto;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import lombok.Builder;

public sealed interface UserResponse permits UserInfo {

    record UserInfo(
        Long id,
        String name,
        String email,
        String profileImageUrl
    ) implements UserResponse {

        @Builder
        public UserInfo {
        }
    }

}
