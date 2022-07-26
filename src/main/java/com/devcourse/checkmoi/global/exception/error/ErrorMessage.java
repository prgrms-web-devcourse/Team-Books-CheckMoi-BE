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
    EXPIRED_ACCESS_TOKEN("만료된 액세스 토큰입니다", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("만료된 리프레쉬 토큰입니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),

    // not found error
    STUDY_NOT_FOUND("해당하는 스터디를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    BOOK_NOT_FOUND("해당하는 책을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    STUDY_JOIN_REQUEST_NOT_FOUND("해당하는 스터디 가입 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND("해당하는 포스트를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("존재하지 않는 사용자입니다", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("해당하는 댓글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // duplicate error
    STUDY_JOIN_REQUEST_DUPLICATE("이미 스터디 가입 요청을 완료했습니다.", HttpStatus.CONFLICT),

    // file error
    NOT_ALLOWED_FILE("허용할 수 없는 파일입니다", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEED("파일 크기는 1MB 를 넘을 수 없습니다", HttpStatus.PAYLOAD_TOO_LARGE),

    // study error
    NOT_ALLOWED_STUDY_STATUS("스터디 상태 변경을 할 수 없습니다", HttpStatus.BAD_REQUEST),
    FINISHED_STUDY("이미 종료된 스터디입니다.", HttpStatus.FORBIDDEN),
    NOT_JOINED_USER("해당 스터디에 참가하지 않은 유저입니다.", HttpStatus.FORBIDDEN),
    NOT_RECRUITING_STUDY("모집중인 스터디가 아닙니다.", HttpStatus.FORBIDDEN),
    STUDY_IS_FULL("해당 스터디는 모집인원이 가득찼습니다.", HttpStatus.FORBIDDEN),
    STUDY_JOIN_MAXIMUM_REACHED("최대 스터디 가입 수에 도달하여 더 이상 가입할 수 없습니다.", HttpStatus.FORBIDDEN),
    // post error
    NOT_ALLOWED_WRITER("게시글 작성 권한이 없습니다", HttpStatus.FORBIDDEN),
    CLOSED_STUDY("종료된 스터디입니다. 자유게시판만 사용가능합니다", HttpStatus.BAD_REQUEST),

    //comment error
    COMMENT_NO_PERMISSION("해당 댓글에 대한 접근 권한이 없습니다", HttpStatus.FORBIDDEN);

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
