package com.devcourse.checkmoi.domain.user.converter;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserInfo userToUserInfo(User user) {
        return UserInfo.builder()
            .name(user.getName())
            .email(user.getEmail().getValue())
            .profileImageUrl(user.getProfileImgUrl())
            .build();
    }

}
