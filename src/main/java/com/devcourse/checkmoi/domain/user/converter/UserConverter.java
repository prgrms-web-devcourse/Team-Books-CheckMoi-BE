package com.devcourse.checkmoi.domain.user.converter;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.UserRole;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import com.devcourse.checkmoi.global.security.oauth.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserInfo userToUserInfo(User user) {
        return UserInfo.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail().getValue())
            .temperature(user.getTemperature())
            .profileImageUrl(user.getProfileImgUrl())
            .build();
    }

    public User profileToUser(UserProfile userProfile) {
        return User.builder()
            .oauthId(userProfile.getOauthId())
            .provider(userProfile.getProvider())
            .name(userProfile.getName())
            .email(new Email(userProfile.getEmail()))
            .profileImgUrl(userProfile.getProfileImgUrl())
            .temperature(36.5f)
            .userRole(UserRole.LOGIN)
            .build();
    }

    public Register userToRegister(User user) {
        return Register.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail().getValue())
            .profileImageUrl(user.getProfileImgUrl())
            .role(user.getUserRole().toString())
            .build();
    }

}
