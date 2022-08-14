package com.devcourse.checkmoi.domain.token.dto;

import java.util.Objects;
import lombok.Builder;

public sealed interface TokenRequest permits TokenRequest.TestToken {

    record TestToken(
        Long accessTime,
        Long refreshTime
    ) implements TokenRequest {

        @Builder
        public TestToken(Long accessTime, Long refreshTime) {
            if (Objects.isNull(accessTime)) {
                accessTime = 30_000L;
            }
            if (Objects.isNull(refreshTime)) {
                refreshTime = accessTime + 60_000L; // access 만료시간 + 1분
            }
            this.accessTime = accessTime;
            this.refreshTime = refreshTime;
        }
    }
}
