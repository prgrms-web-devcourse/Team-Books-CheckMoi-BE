package com.devcourse.checkmoi.domain.study.service.study;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;

public interface StudyCommandService {

    Long createStudy(Create request);
}
