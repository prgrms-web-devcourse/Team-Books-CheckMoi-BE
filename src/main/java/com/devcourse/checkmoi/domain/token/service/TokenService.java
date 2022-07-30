package com.devcourse.checkmoi.domain.token.service;

import com.devcourse.checkmoi.domain.token.dto.TokenRequest;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.AccessToken;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.Tokens;
import com.devcourse.checkmoi.domain.token.model.Token;
import com.devcourse.checkmoi.domain.token.repository.TokenRepository;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.global.security.jwt.JwtTokenProvider;
import com.devcourse.checkmoi.global.security.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Tokens createToken(Register user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.id());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        tokenRepository.save(new Token(refreshToken, user.id()));
        return new Tokens(accessToken, refreshToken);
    }

    @Transactional
    public AccessToken refreshAccessToken(String accessToken,
        TokenRequest.RefreshToken refreshTokenRequest) {
        jwtTokenProvider.validateAccessToken(accessToken);

        String refreshToken = refreshTokenRequest.refreshToken();
        jwtTokenProvider.validateToken(refreshToken);

        Claims claims = jwtTokenProvider.getClaims(accessToken);
        Long userId = claims.get("userId", Long.class);
        var findRefreshToken = tokenRepository.findTokenByUserId(userId)
            .map(Token::getRefreshToken)
            .orElseThrow(InvalidTokenException::new);

        if (!refreshToken.equals(findRefreshToken)) {
            throw new InvalidTokenException();
        }

        var newAccessToken = jwtTokenProvider.createAccessToken(userId);
        return new AccessToken(newAccessToken);
    }

    @Transactional
    public void deleteTokenByUserId(Long userId) {
        if (!tokenRepository.existsByUserId(userId)) {
            throw new UserNotFoundException();
        }
        tokenRepository.deleteByUserId(userId);
    }
}
