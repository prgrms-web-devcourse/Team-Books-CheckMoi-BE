package com.devcourse.checkmoi.global.security.config;

import com.devcourse.checkmoi.global.security.handler.ExceptionHandlerFilter;
import com.devcourse.checkmoi.global.security.handler.JwtAuthenticationEntryPoint;
import com.devcourse.checkmoi.global.security.handler.OAuthAuthenticationSuccessHandler;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthAuthenticationSuccessHandler oAuth2SuccessHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .antMatchers("/static/**", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // cors
            .cors().and()

            // permission
            .authorizeHttpRequests()
            .anyRequest().permitAll()
            .and()

            // oauth
            .oauth2Login()
            .successHandler(oAuth2SuccessHandler)
            .and()

            // turnOff filter
            .csrf().disable()
            .httpBasic().disable()
            .rememberMe().disable()
            .logout().disable()
            .requestCache().disable()
            .formLogin().disable()
            .headers().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // jwt authentication entry point
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()

            .addFilterBefore(jwtAuthenticationFilter,
                OAuth2AuthorizationRequestRedirectFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }


}
