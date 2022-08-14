package com.devcourse.checkmoi.global.scheduler;

import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.dto.ExpiredStudies;

public interface ScheduleManager {

    void updateStudyWithMembers(Long studyId, StudyStatus studyStatus,
        StudyMemberStatus memberStatus);

    void updateStudy(Long studyId, StudyStatus studyStatus);

    ExpiredStudies getAllStudiesToBeProcessed(StudyStatus toStatus);
}
