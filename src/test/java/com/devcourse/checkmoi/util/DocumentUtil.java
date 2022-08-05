package com.devcourse.checkmoi.util;

import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.snippet.Attributes;

public abstract class DocumentUtil {

    public static Attributes.Attribute getDateFormat() {
        return key("format").value("yyyy-MM-dd");
    }
}
