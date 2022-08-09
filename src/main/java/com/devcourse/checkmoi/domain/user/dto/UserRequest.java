package com.devcourse.checkmoi.domain.user.dto;

import lombok.Builder;

public sealed interface UserRequest permits UserRequest.Edit {

    record Edit(
        String name,
        String image
    ) implements UserRequest {

        @Builder
        public Edit {
        }
    }

}
