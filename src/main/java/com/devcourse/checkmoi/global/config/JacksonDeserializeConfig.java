package com.devcourse.checkmoi.global.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonDeserializeConfig {

    @Bean
    public Module jsonMapperJava8DateTimeModule() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
                String changeDate = p.getValueAsString().replace("/", "-");
                return LocalDate.parse(changeDate,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });

        return module;
    }
}
