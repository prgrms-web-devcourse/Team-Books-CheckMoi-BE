package com.devcourse.checkmoi.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {

    // global error
    INVALID_INPUT("유효하지 않은 입력값 입니다.", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("찾을 수 없는 엔티티입니다.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("서버 에러 입니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // authentication error
    ACCESS_DENIED("접근 권한이 없습니다", HttpStatus.FORBIDDEN),

    // not found error
    STUDY_NOT_FOUND("해당하는 스터디를 찾을 수 없습니다", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    ErrorMessage(final String message, final HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
