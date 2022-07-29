package com.devcourse.checkmoi.domain.study.service.study;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.CreateStudy;

public interface StudyCommandService {

    Long createStudy(CreateStudy request);
}
