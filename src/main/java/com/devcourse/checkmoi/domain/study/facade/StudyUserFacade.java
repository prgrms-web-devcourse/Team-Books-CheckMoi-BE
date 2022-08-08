package com.devcourse.checkmoi.domain.study.facade;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;

public interface StudyUserFacade {

    MyStudies getMyStudies(Long id);
}
