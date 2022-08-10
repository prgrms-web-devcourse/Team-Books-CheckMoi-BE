package com.devcourse.checkmoi.global.exception.error;

import java.util.List;
import org.springframework.validation.BindingResult;

public class ErrorResponse {

    private final List<Error> errors;

    private ErrorResponse(List<Error> errors) {
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorMessage errorMessage) {
        return new ErrorResponse(
            List.of(new Error(errorMessage.getMessage()))
        );
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(
            ErrorField.of(bindingResult));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
