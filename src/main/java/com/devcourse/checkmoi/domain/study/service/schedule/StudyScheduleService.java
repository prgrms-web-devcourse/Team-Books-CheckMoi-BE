package com.devcourse.checkmoi.domain.study.service.schedule;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.ExpiredStudies;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.global.scheduler.ScheduleManager;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StudyScheduleService implements ScheduleManager {

    private final StudyCommandService studyCommandService;

    private final StudyQueryService studyQueryService;

    @Override
    @Transactional
    public void updateStudyWithMembers(Long studyId, StudyStatus studyStatus,
        StudyMemberStatus memberStatus) {

        studyCommandService.updateStudyStatus(studyId, studyStatus);

        studyCommandService.updateApplicants(studyId, StudyMemberStatus.DENIED);
    }

    @Override
    public ExpiredStudies getAllStudies() {

        return studyQueryService.getAllExpiredStudies(LocalDate.now(), StudyStatus.IN_PROGRESS);
    }
}
