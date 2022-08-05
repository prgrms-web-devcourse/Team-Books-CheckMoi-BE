package com.devcourse.checkmoi.domain.book.model;

import java.time.format.DateTimeParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PublishedDateTest {

    @Test
    @DisplayName("yyyyMMdd 가 아닌 데이트포맷의 문자열이 들어오면 생성 실패한다")
    void createFail() {
        String date = "2010/10/11";

        Assertions.assertThatThrownBy(() -> new PublishedDate(date))
            .isInstanceOf(DateTimeParseException.class);
    }
}