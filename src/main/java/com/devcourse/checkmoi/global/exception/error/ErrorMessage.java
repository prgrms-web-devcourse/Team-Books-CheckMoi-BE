package com.devcourse.checkmoi.global.exception.error;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {

    // global error
    INVALID_INPUT("유효하지 않은 입력값 입니다.", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("찾을 수 없는 엔티티입니다.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("서버 에러 입니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // authentication error
    ACCESS_DENIED("접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    LOGIN_REQUIRED("로그인이 필요합니다", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("만료된 토큰입니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),


    // not found error
    STUDY_NOT_FOUND("해당하는 스터디를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    BOOK_NOT_FOUND("해당하는 책을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    STUDY_JOIN_REQUEST_NOT_FOUND("해당하는 스터디 가입 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND("해당하는 포스트를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("해당하는 댓글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // no permission
    // duplicate error
    STUDY_JOIN_REQUEST_DUPLICATE("이미 스터디 가입 요청을 완료했습니다.", HttpStatus.CONFLICT),

    // file error
    NOT_ALLOWED_FILE("허용할 수 없는 파일입니다", HttpStatus.BAD_REQUEST),

    // study error
    NOT_ALLOWED_STUDY_STATUS("스터디 상태 변경을 할 수 없습니다", HttpStatus.BAD_REQUEST),
    FINISHED_STUDY("이미 종료된 스터디입니다.", HttpStatus.FORBIDDEN),
    NOT_PARTICIPATE_STUDY_USER("해당 스터디에 참가하지 않은 유저입니다.", HttpStatus.FORBIDDEN),
    // post error
    NOT_ALLOWED_WRITER("게시글 작성 권한이 없습니다", HttpStatus.FORBIDDEN);

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
