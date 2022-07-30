package com.devcourse.checkmoi.domain.token.api;

import com.devcourse.checkmoi.domain.token.dto.TokenRequest;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.AccessToken;
import com.devcourse.checkmoi.domain.token.service.TokenService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import com.devcourse.checkmoi.global.security.util.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenApi {

    private final TokenService tokenService;

    @PostMapping("/tokens")
    public ResponseEntity<SuccessResponse<AccessToken>> refreshAccessToken(
        HttpServletRequest httpServletRequest,
        @Valid @RequestBody TokenRequest.RefreshToken refreshToken
    ) {
        String accessToken = AuthorizationExtractor.extract(httpServletRequest);
        return ResponseEntity.ok(
            new SuccessResponse<>(tokenService.refreshAccessToken(accessToken, refreshToken)));
    }

    @DeleteMapping("/tokens")
    public ResponseEntity<Void> deleteRefreshToken(
        @AuthenticationPrincipal JwtAuthentication user) {
        tokenService.deleteTokenByUserId(user.id());
        return ResponseEntity.noContent().build();
    }
}
