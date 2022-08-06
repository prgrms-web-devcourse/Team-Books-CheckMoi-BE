package com.devcourse.checkmoi.domain.study.service;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface StudyQueryService {

    Studies getStudies(Long bookId, Pageable pageable);

    StudyDetailWithMembers getStudyInfoWithMembers(Long studyId);

    StudyAppliers getStudyAppliers(Long userId, Long studyId);

    List<List<MyStudyInfo>> getMyStudies(Long userId);
}
