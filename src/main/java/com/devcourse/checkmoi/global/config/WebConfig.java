package com.devcourse.checkmoi.global.config;

import static org.springframework.http.HttpHeaders.LOCATION;
import com.devcourse.checkmoi.global.config.property.CorsConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CorsConfigProperties.class)
public class WebConfig implements WebMvcConfigurer {

    private final CorsConfigProperties corsConfigProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping(corsConfigProperties.api())
            .allowedOrigins(corsConfigProperties.origin())
            .allowedMethods(corsConfigProperties.method())
            .allowCredentials(true).maxAge(3600)
            .exposedHeaders(LOCATION);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
