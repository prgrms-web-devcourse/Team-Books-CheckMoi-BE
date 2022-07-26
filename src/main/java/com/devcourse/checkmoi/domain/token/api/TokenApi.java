package com.devcourse.checkmoi.domain.token.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.devcourse.checkmoi.domain.token.dto.AccessTokenResponse;
import com.devcourse.checkmoi.domain.token.dto.RefreshTokenRequest;
import com.devcourse.checkmoi.domain.token.service.TokenService;
import com.devcourse.checkmoi.global.model.ApiResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import com.devcourse.checkmoi.global.security.util.AuthorizationExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TokenApi {

    private final TokenService tokenService;

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refreshAccessToken(
        HttpServletRequest httpServletRequest,
        @Valid @RequestBody RefreshTokenRequest refreshToken
    ) {
        String accessToken = AuthorizationExtractor.extract(httpServletRequest);
        return ResponseEntity.ok(new ApiResponse<>(tokenService.refreshAccessToken(accessToken, refreshToken)));
    }

    @DeleteMapping("/tokens")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefreshToken(@AuthenticationPrincipal JwtAuthentication user) {
        tokenService.deleteTokenByUserId(user.id());
    }
}
