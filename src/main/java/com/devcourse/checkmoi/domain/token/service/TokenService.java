package com.devcourse.checkmoi.domain.token.service;

import com.devcourse.checkmoi.domain.token.dto.TokenResponse.AccessToken;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.token.model.Token;
import com.devcourse.checkmoi.domain.token.repository.TokenRepository;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.global.security.jwt.JwtTokenProvider;
import com.devcourse.checkmoi.global.security.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenWithUserInfo createTokenWithRegisterUser(Register user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.id(), user.role());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        Token token = tokenRepository.findTokenByUserId(user.id())
            .orElseGet(() -> tokenRepository.save(new Token(refreshToken, user.id())));

        token.refresh(refreshToken);

        UserInfo userinfo = UserInfo.builder()
            .id(user.id())
            .name(user.name())
            .email(user.email())
            .temperature(36.5f)
            .image(user.profileImageUrl())
            .build();

        return new TokenWithUserInfo(accessToken, refreshToken, userinfo);
    }

    @Transactional
    public AccessToken refreshAccessToken(String accessToken) {

        Claims claims = jwtTokenProvider.parseUserClaimsFromExpiredAccessToken(accessToken)
            .orElseThrow(InvalidTokenException::new);
        Long userId = claims.get("userId", Long.class);
        String role = claims.get("role", String.class);

        String findRefreshToken = tokenRepository.findTokenByUserId(userId)
            .map(Token::getRefreshToken)
            .orElseThrow(InvalidTokenException::new);

        jwtTokenProvider.validateRefreshToken(findRefreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userId, role);
        return new AccessToken(newAccessToken);
    }

    @Transactional
    public void deleteTokenByUserId(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public String createTemporaryAccessToken(Long userId) {
        String refreshToken = jwtTokenProvider.createRefreshToken();

        Token token = tokenRepository.findTokenByUserId(userId)
            .orElseGet(() -> tokenRepository.save(new Token(refreshToken, userId)));

        token.refresh(refreshToken);
        return jwtTokenProvider.createAccessToken(userId, "ROLE_ADMIN");
    }

    @Transactional
    public String createTestToken(Long userId, Long accessTime, Long refreshTime) {
        log.info("테스트용 토큰을 발급합니다 - userId : {}, "
            + "액세스 토큰 만료시간 {}, 리프레시 토큰 만료시간 {}", userId, accessTime, refreshTime);

        String refreshToken = jwtTokenProvider.createTestToken(
            userId, "ROLE_LOGIN", refreshTime);

        Token token = tokenRepository.findTokenByUserId(userId)
            .orElseGet(() -> tokenRepository.save(new Token(refreshToken, userId)));
        token.refresh(refreshToken);

        return jwtTokenProvider.createTestToken(userId, "ROLE_LOGIN", accessTime);
    }

}
