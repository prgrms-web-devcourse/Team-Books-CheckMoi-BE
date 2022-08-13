package com.devcourse.checkmoi.global.exception.handler;

import com.devcourse.checkmoi.global.exception.BusinessException;
import com.devcourse.checkmoi.global.exception.error.ErrorMessage;
import com.devcourse.checkmoi.global.exception.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_LOG_MESSAGE = "[ERROR] {} : {}";

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage());

        ErrorMessage errorMessage = ErrorMessage.ACCESS_DENIED;

        return ResponseEntity
            .status(errorMessage.getStatus())
            .body(ErrorResponse.of(errorMessage));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage());

        ErrorMessage errorMessage = e.getErrorMessage();
        ErrorResponse response = ErrorResponse.of(errorMessage);

        return new ResponseEntity<>(response, errorMessage.getStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage());

        ErrorResponse response = ErrorResponse.of(ErrorMessage.INTERNAL_SERVER_ERROR);

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.warn(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getMessage());

        ErrorResponse response = ErrorResponse.of(e);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<ErrorResponse> handleFileSizeException(MultipartException e) {
        log.error("{}", e.getMessage());

        ErrorResponse response = ErrorResponse.of(ErrorMessage.FILE_SIZE_EXCEED);

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(response);
    }

}
