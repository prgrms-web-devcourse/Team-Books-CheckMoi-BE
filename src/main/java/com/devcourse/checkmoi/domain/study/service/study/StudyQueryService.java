package com.devcourse.checkmoi.domain.study.service.study;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import org.springframework.data.domain.Pageable;

public interface StudyQueryService {

    Studies getStudies(Long bookId, Pageable pageable);
}
