package com.devcourse.checkmoi.domain.study.service.schedule;

import com.devcourse.checkmoi.domain.scheduler.ScheduleManager;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.ExpiredStudies;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyScheduleService implements ScheduleManager {

    private final StudyCommandService studyCommandService;

    private final StudyQueryService studyQueryService;

    @Override
    public void updateStudyWithMembers(Long studyId, StudyStatus studyStatus,
        StudyMemberStatus memberStatus) {

    }

    @Override
    public ExpiredStudies getAllStudies() {
        return null;
    }
}
