package com.devcourse.checkmoi.global.security.oauth;

import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.token.service.TokenService;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthService {

    private final TokenService tokenService;

    private final UserCommandService userCommandService;

    @Transactional
    public TokenWithUserInfo register(UserProfile userProfile) {
        Register registeredUser = userCommandService.registerAccount(userProfile);
        return tokenService.createTokenWithRegisterUser(registeredUser);
    }

}
