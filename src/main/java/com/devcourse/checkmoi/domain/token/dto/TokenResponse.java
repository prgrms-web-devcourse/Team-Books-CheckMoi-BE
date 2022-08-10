package com.devcourse.checkmoi.domain.token.dto;

import com.devcourse.checkmoi.domain.token.dto.TokenResponse.AccessToken;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.Tokens;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import lombok.Builder;

public sealed interface TokenResponse permits Tokens, AccessToken, TokenWithUserInfo {

    record Tokens(
        String accessToken,
        String refreshToken
    ) implements TokenResponse {

        @Builder
        public Tokens {
        }
    }

    record AccessToken(
        String accessToken
    ) implements TokenResponse {

        @Builder
        public AccessToken {
        }
    }

    record TokenWithUserInfo(
        String accessToken,
        String refreshToken,
        UserInfo userInfo
    ) implements TokenResponse {

        @Builder
        public TokenWithUserInfo {
        }
    }
}
