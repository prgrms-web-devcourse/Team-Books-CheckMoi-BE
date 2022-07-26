package com.devcourse.checkmoi.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    GUEST("ROLE_GUEST"),
    HOST("ROLE_HOST");

    private final String grantedAuthority;

}
