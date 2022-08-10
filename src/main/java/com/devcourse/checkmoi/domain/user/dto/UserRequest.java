package com.devcourse.checkmoi.domain.user.dto;

import javax.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

public sealed interface UserRequest permits UserRequest.Edit {

    record Edit(
        @Size(max = 20, message = "이름은 20자를 넘길 수 없습니다.")
        String name,
        
        @URL(message = "올바른 이미지 URL이 필요합니다.")
        String image
    ) implements UserRequest {

        @Builder
        public Edit {
        }
    }

}
