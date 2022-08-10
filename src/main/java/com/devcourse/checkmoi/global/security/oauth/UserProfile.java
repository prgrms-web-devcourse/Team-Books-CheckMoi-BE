package com.devcourse.checkmoi.global.security.oauth;

import com.devcourse.checkmoi.domain.user.model.UserRole;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfile {

    private final String oauthId;

    private final String provider;

    private final String name;

    private final String email;

    private final String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.LOGIN;

    @Builder
    public UserProfile(String oauthId, String provider, String name, String email,
        String profileImgUrl) {
        this.oauthId = oauthId;
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
    }

}
