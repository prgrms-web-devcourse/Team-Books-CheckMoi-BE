package com.devcourse.checkmoi.global.config;

import static org.springframework.http.HttpHeaders.LOCATION;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,POST,PUT,DELETE,HEAD,OPTIONS";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
            .exposedHeaders(LOCATION);
    }

}
