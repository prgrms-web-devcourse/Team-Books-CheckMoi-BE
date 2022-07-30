package com.devcourse.checkmoi.domain.token.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface TokenRequest permits TokenRequest.RefreshToken {

    record RefreshToken(
        @NotBlank(message = "refreshToken은 비어있을 수 없습니다.")
        String refreshToken
    ) implements TokenRequest {

        @Builder
        public RefreshToken {
        }
    }
}
