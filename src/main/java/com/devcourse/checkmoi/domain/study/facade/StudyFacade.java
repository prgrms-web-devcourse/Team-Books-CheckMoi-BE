package com.devcourse.checkmoi.domain.study.facade;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import org.springframework.data.domain.Pageable;

public interface StudyFacade {

    MyStudies getMyStudies(Long id);

    Long createStudy(Create request, Long userId);

    Studies getStudies(Long bookId, Pageable pageable);

    Long requestStudyJoin(Long studyId, Long userId);
}
