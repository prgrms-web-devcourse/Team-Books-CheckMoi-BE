package com.devcourse.checkmoi.global.security.oauth;

import com.devcourse.checkmoi.domain.token.dto.TokenResponse.Tokens;
import com.devcourse.checkmoi.domain.token.service.TokenService;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthService {

    private final TokenService tokenService;

    private final UserService userService;

    @Transactional
    public Tokens register(UserProfile userProfile) {
        Register registeredUser = userService.register(userProfile);
        return tokenService.createToken(registeredUser);
    }

}
