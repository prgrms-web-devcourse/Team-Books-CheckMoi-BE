package com.devcourse.checkmoi.global.log;

import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthToMdcFilter implements Filter {

    private static final String MDC_USER_KEY = "LoginUserId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

            putPrincipalToMdc(principal);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_KEY);
        }
    }

    private void putPrincipalToMdc(Object principal) {

        if (JwtAuthentication.class.isAssignableFrom(principal.getClass())) {
            MDC.put(MDC_USER_KEY,
                ((JwtAuthentication) principal).id().toString());
        }
    }
}
