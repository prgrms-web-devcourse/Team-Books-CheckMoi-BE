package com.devcourse.checkmoi.global.security.jwt;

import com.devcourse.checkmoi.global.security.jwt.util.AuthorizationExtractor;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().endsWith("tokens")
            && request.getMethod().equalsIgnoreCase("GET");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain)
        throws ServletException, IOException {

        String accessToken = getAccessToken(req);

        if (accessToken != null) {
            JwtAuthenticationToken authentication = createAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(req, res);
    }

    private String getAccessToken(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        if (token != null) {
            jwtTokenProvider.validateToken(token);
        }
        return token;
    }

    private JwtAuthenticationToken createAuthentication(String accessToken) {
        Claims claims = jwtTokenProvider.getClaims(accessToken);
        Long userId = claims.get("userId", Long.class);
        String role = claims.get("role", String.class);

        return new JwtAuthenticationToken(
            new JwtAuthentication(accessToken, userId), null, toAuthorities(role)
        );
    }

    private List<GrantedAuthority> toAuthorities(String role) {
        return List.of(new SimpleGrantedAuthority(role));
    }

}
