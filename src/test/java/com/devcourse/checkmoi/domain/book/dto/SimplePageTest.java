package com.devcourse.checkmoi.domain.book.dto;

import com.devcourse.checkmoi.global.model.SimplePage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimplePageTest {

    @Test
    @DisplayName("유효하지 않은 페이지,사이즈 값을 전달받을 경우 페이지 정보는 전달받은 값이 아닌, 디폴트 값을 갖는다")
    void givenInvalidValueThenCreateDefault() {
        int size = 0;
        int page = 0;

        SimplePage pageable = SimplePage.builder()
            .size(0)
            .page(0)
            .build();

        Assertions.assertThat(pageable.getPage())
            .isNotEqualTo(page);
    }
}