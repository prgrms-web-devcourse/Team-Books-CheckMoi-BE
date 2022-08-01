package com.devcourse.checkmoi.global.security.handler;

import com.devcourse.checkmoi.global.security.oauth.OAuthProvider;
import com.devcourse.checkmoi.global.security.oauth.OAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthService oauthService;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {

        var providerName = parseProviderName(request);
        var principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauth) {

            var userProfile = OAuthProvider
                .getProviderFromName(providerName)
                .toUserProfile(oauth);

            var tokenResponse = oauthService.register(userProfile);

            String uri = UriComponentsBuilder.fromUriString("http://localhost:3000/login")
                .queryParam("token", tokenResponse.accessToken())
                .build()
                .toUriString();
            response.sendRedirect(uri);

            log.info("user : " + tokenResponse.userInfo());
            log.info("a token : " + tokenResponse.accessToken());
            log.info("r token : " + tokenResponse.refreshToken());

        }
    }

    private String parseProviderName(HttpServletRequest request) {
        var splitURI = request.getRequestURI().split("/");
        return splitURI[splitURI.length - 1];
    }

}
