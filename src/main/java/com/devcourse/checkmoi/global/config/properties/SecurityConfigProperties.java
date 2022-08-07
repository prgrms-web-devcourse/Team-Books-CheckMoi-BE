package com.devcourse.checkmoi.global.config.properties;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "security")
public record SecurityConfigProperties(PatternsConfigures patterns) {

    public SecurityConfigProperties(PatternsConfigures patterns) {
        this.patterns = patterns;
    }

    public record PatternsConfigures(Map<String, String[]> ignoring,
                                     Map<String, String[]> permitAll) {

    }
}
