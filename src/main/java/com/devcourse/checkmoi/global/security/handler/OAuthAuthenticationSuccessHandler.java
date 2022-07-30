package com.devcourse.checkmoi.global.security.handler;

import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.oauth.OAuthProvider;
import com.devcourse.checkmoi.global.security.oauth.OAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthService oauthService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        var providerName = parseProviderName(request);
        var principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauth) {

            var userProfile = OAuthProvider
                .getProviderFromName(providerName)
                .toUserProfile(oauth);

            var tokenResponse = oauthService.register(userProfile);

            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                .write(objectMapper.writeValueAsString(new SuccessResponse<>(tokenResponse)));
        }
    }

    private String parseProviderName(HttpServletRequest request) {
        var splitURI = request.getRequestURI().split("/");
        return splitURI[splitURI.length - 1];
    }

}
