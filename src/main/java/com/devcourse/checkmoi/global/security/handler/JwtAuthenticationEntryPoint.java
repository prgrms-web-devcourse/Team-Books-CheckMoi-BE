package com.devcourse.checkmoi.global.security.handler;

import com.devcourse.checkmoi.global.exception.ErrorMessage;
import com.devcourse.checkmoi.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ERROR_LOG_MESSAGE = "[ERROR] {} : {}";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        log.info(ERROR_LOG_MESSAGE,
            authException.getClass().getSimpleName(),
            ErrorMessage.LOGIN_REQUIRED.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
            .write(objectMapper.writeValueAsString(ErrorResponse.of(ErrorMessage.LOGIN_REQUIRED)));
    }

}
