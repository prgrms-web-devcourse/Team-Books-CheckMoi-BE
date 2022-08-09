package com.devcourse.checkmoi.global.security.oauth.handler;

import com.devcourse.checkmoi.global.security.oauth.OAuthProvider;
import com.devcourse.checkmoi.global.security.oauth.OAuthService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${front-url}")
    private String frontUrl;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {

        String providerName = parseProviderName(request);
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oauth) {

            var userProfile = OAuthProvider
                .getProviderFromName(providerName)
                .toUserProfile(oauth);

            var tokenResponse = oauthService.register(userProfile);

            String uri = UriComponentsBuilder.fromUriString(frontUrl + "/login")
                .queryParam("token", tokenResponse.accessToken())
                .build()
                .toUriString();

            response.setHeader("Authorization", "Bearer " + tokenResponse.accessToken());
            response.sendRedirect(uri);

            log.info("login user : " + tokenResponse.userInfo());
        }
    }

    private String parseProviderName(HttpServletRequest request) {
        var splitURI = request.getRequestURI().split("/");
        return splitURI[splitURI.length - 1];
    }

}
