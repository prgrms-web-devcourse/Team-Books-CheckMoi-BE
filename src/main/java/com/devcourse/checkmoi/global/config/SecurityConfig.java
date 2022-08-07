package com.devcourse.checkmoi.global.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import com.devcourse.checkmoi.global.config.properties.SecurityConfigProperties;
import com.devcourse.checkmoi.global.security.handler.ExceptionHandlerFilter;
import com.devcourse.checkmoi.global.security.handler.JwtAuthenticationEntryPoint;
import com.devcourse.checkmoi.global.security.handler.OAuthAuthenticationSuccessHandler;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class SecurityConfig {

    private final OAuthAuthenticationSuccessHandler oAuth2SuccessHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final SecurityConfigProperties securityConfigProperties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
            .antMatchers(securityConfigProperties.patterns().ignoring().get("ALL"))
            .antMatchers(GET, securityConfigProperties.patterns().ignoring().get("GET"))
            .antMatchers(PUT, securityConfigProperties.patterns().ignoring().get("PUT"))
            .antMatchers(POST, securityConfigProperties.patterns().ignoring().get("POST"))
            .antMatchers(PATCH, securityConfigProperties.patterns().ignoring().get("PATCH"))
            .antMatchers(DELETE, securityConfigProperties.patterns().ignoring().get("DELETE"))
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // cors
            .cors().and()

            // permission
            .authorizeHttpRequests()
            .antMatchers(
                securityConfigProperties.patterns().permitAll().get("ALL")).permitAll()
            .antMatchers(GET,
                securityConfigProperties.patterns().permitAll().get("GET")).permitAll()
            .antMatchers(PUT,
                securityConfigProperties.patterns().permitAll().get("PUT")).permitAll()
            .antMatchers(POST,
                securityConfigProperties.patterns().permitAll().get("POST")).permitAll()
            .antMatchers(PATCH,
                securityConfigProperties.patterns().permitAll().get("PATCH")).permitAll()
            .antMatchers(DELETE,
                securityConfigProperties.patterns().permitAll().get("DELETE")).permitAll()
            .antMatchers(OPTIONS,
                securityConfigProperties.patterns().permitAll().get("OPTIONS")).permitAll()
            .anyRequest().authenticated()
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
