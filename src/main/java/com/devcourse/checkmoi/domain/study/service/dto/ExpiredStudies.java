package com.devcourse.checkmoi.domain.study.service.dto;

import java.util.List;

public record ExpiredStudies(
    List<Long> studies
) {

}
