package com.devcourse.checkmoi.domain.study.facade;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;

public interface StudyFacade {

    MyStudies getMyStudies(Long id);

    Long createStudy(Create request, Long userId);
}
