package com.devcourse.checkmoi.global.security.jwt;

import com.devcourse.checkmoi.global.security.jwt.exception.ExpiredAccessTokenException;
import com.devcourse.checkmoi.global.security.jwt.exception.ExpiredRefreshTokenException;
import com.devcourse.checkmoi.global.security.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String issuer;

    private final String secretKey;

    private final long accessTokenValidTime;

    private final long refreshTokenValidTime;

    public JwtTokenProvider(
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.expire-length}") long accessTokenValidTime,
        @Value("${jwt.refresh-token.expire-length}") long refreshTokenValidTime
    ) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    // create Token
    private String createToken(Map<String, Object> claims, long expireTime) {
        var now = new Date();
        return Jwts.builder()
            .setIssuer(issuer)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + expireTime))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    public String createAccessToken(long payload, String role) {
        Map<String, Object> claims = Map.of("userId", payload, "role", role);
        return createToken(claims, accessTokenValidTime);
    }

    public String createRefreshToken() {
        String payload = UUID.randomUUID().toString();
        Claims claims = Jwts.claims().setSubject(payload);
        return createToken(claims, refreshTokenValidTime);
    }

    // validate Token
    private Jws<Claims> parsingToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token);
    }

    public Claims getClaims(String token) {
        return parsingToken(token).getBody();
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            parsingToken(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
        log.info("refreshToken 검증 성공! 새로운 accessToken 을 발급합니다.");
    }

    public void validateAccessToken(String accessToken) {
        try {
            parsingToken(accessToken);
        } catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    public Optional<Claims> parseUserClaimsFromExpiredAccessToken(String accessToken) {
        Optional<Claims> claims;
        try {
            claims = Optional.of(parsingToken(accessToken).getBody());
        } catch (ExpiredJwtException e) {
            Long userId = e.getClaims().get("userId", Long.class);
            log.info("userId - {}의 accessToken 유효기간이 지났습니다. refreshToken 검증을 시작하겠습니다..", userId);
            return Optional.of(e.getClaims());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
        return claims;
    }

    // INFO : Token Test Method
    public String createTestToken(long payload, String role, long expireTime) {
        Map<String, Object> claims = Map.of("userId", payload, "role", role);
        return createToken(claims, expireTime);
    }
}
