package com.devcourse.checkmoi.global.exception;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

public class ErrorField extends Error {

    public ErrorField(String message) {
        super(message);
    }

    public static List<Error> of(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
            .map(error ->
                new ErrorField(
                    error.getDefaultMessage()))
            .collect(Collectors.toList());
    }

    private static Object rejectedValue(Object value) {
        if (Collection.class.isAssignableFrom(value.getClass())) {
            return ((Collection<?>) value).size();
        }
        return value;
    }
}
