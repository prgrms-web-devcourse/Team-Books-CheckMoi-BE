package com.devcourse.checkmoi.domain.scheduler;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;

public interface ScheduleManager {

    void updateStudyWithMembers(Long studyId, StudyStatus studyStatus,
        StudyMemberStatus memberStatus);

    StudyResponse.ExpiredStudies getAllStudies();
}
