package com.devcourse.checkmoi.domain.user.dto;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import lombok.Builder;

public sealed interface UserResponse permits UserInfo, Register {

    record UserInfo(
        String name,
        String email,
        String profileImageUrl
    ) implements UserResponse {

        @Builder
        public UserInfo {
        }
    }

    record Register(
        Long id,// TODO: name? id? Long? String?
        String email,
        String profileImageUrl
    ) implements UserResponse {

        @Builder
        public Register {
        }
    }
}
