package com.devcourse.checkmoi.domain.study.model;

import com.devcourse.checkmoi.global.annotation.CodeMappable;

public enum StudyMemberStatus implements CodeMappable {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    DENIED("DENIED"),
    OWNED("OWNED");

    private final String code;

    StudyMemberStatus(String code) {
        this.code = code;
    }

    @Override
    public String getMappingCode() {
        return this.code;
    }
}
