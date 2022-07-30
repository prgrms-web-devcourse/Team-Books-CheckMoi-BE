package com.devcourse.checkmoi.domain.user.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {
    GUEST("ROLE_GUEST"),
    HOST("ROLE_HOST");

    private final String grantedAuthority;

    public String getGrantedAuthority() {
        return grantedAuthority;
    }
}
