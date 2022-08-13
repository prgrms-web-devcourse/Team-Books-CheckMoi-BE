package com.devcourse.checkmoi.domain.token.api;

import com.devcourse.checkmoi.domain.token.dto.TokenResponse.AccessToken;
import com.devcourse.checkmoi.domain.token.service.TokenService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import com.devcourse.checkmoi.global.security.jwt.util.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenApi {

    private final TokenService tokenService;

    @GetMapping("/tokens")
    public ResponseEntity<SuccessResponse<AccessToken>> refreshAccessToken(
        HttpServletRequest httpServletRequest
    ) {
        String accessToken = AuthorizationExtractor.extract(httpServletRequest);

        return ResponseEntity.ok(
            new SuccessResponse<>(tokenService.refreshAccessToken(accessToken)));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> deleteRefreshToken(
        @AuthenticationPrincipal JwtAuthentication user
    ) {
        tokenService.deleteTokenByUserId(user.id());
        return ResponseEntity.noContent().build();
    }

    // INFO: 편의메서드 입니다.
    @GetMapping("/tokens/{userId}")
    public String createTemporaryAccessToken(@PathVariable Long userId) {
        return tokenService.createTemporaryAccessToken(userId);
    }

    // INFO: 테스트용 메서드 입니다.
    @GetMapping("/tokens/test")
    public String createTestToken() {
        return tokenService.createTestToken();
    }

}
