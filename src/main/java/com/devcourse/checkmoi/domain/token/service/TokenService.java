package com.devcourse.checkmoi.domain.token.service;

import com.devcourse.checkmoi.domain.token.dto.AccessTokenResponse;
import com.devcourse.checkmoi.domain.token.dto.RefreshTokenRequest;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse;
import com.devcourse.checkmoi.domain.token.entity.Token;
import com.devcourse.checkmoi.domain.token.repository.TokenRepository;
import com.devcourse.checkmoi.domain.user.dto.response.UserRegisterResponse;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.global.security.jwt.JwtTokenProvider;
import com.devcourse.checkmoi.global.security.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponse createToken(UserRegisterResponse user) {
        var accessToken = jwtTokenProvider.createAccessToken(user.id(), user.role());
        var refreshToken = jwtTokenProvider.createRefreshToken();
        tokenRepository.save(new Token(refreshToken, user.id()));
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public AccessTokenResponse refreshAccessToken(String accessToken, RefreshTokenRequest refreshTokenRequest) {
        jwtTokenProvider.validateAccessToken(accessToken);

        var refreshToken = refreshTokenRequest.refreshToken();
        jwtTokenProvider.validateToken(refreshToken);

        Claims claims = jwtTokenProvider.getClaims(accessToken);
        var userId = claims.get("userId", Long.class);
        var findRefreshToken = tokenRepository.findTokenByUserId(userId)
            .map(Token::getRefreshToken)
            .orElseThrow(InvalidTokenException::new);

        if (!refreshToken.equals(findRefreshToken)) {
            throw new InvalidTokenException();
        }

        var role = claims.get("role", String.class);
        var newAccessToken = jwtTokenProvider.createAccessToken(userId, role);
        return new AccessTokenResponse(newAccessToken);
    }

    @Transactional
    public void deleteTokenByUserId(Long userId) {
        if (!tokenRepository.existsByUserId(userId)) {
            throw new UserNotFoundException();
        }
        tokenRepository.deleteByUserId(userId);
    }
}
