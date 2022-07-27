package com.devcourse.checkmoi.domain.token.dto;

import javax.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "refreshToken은 비어있을 수 없습니다.")
    String refreshToken
) {

}
