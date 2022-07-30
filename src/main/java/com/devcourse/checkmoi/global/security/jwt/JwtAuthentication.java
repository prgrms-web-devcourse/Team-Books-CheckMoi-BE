package com.devcourse.checkmoi.global.security.jwt;

import com.devcourse.checkmoi.global.security.jwt.exception.InvalidTokenException;

import java.util.Objects;

public record JwtAuthentication(String token, Long id) {

    public static final long MIN_ID_VALUE = 1L;

    public JwtAuthentication {
        validateToken(token);
        validateUserId(id);
    }

    private void validateToken(String token) {
        if (Objects.isNull(token) || token.isBlank()) {
            throw new InvalidTokenException();
        }
    }

    private void validateUserId(Long id) {
        if (id == null || id < MIN_ID_VALUE) {
            throw new InvalidTokenException();
        }
    }

}
