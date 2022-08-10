package com.devcourse.checkmoi.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "cors.allowed")
public record CorsConfigProperties(
    String api,
    String[] origin,
    String[] method
) {

}
