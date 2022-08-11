package com.devcourse.checkmoi.domain.scheduler;

import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final ScheduleManager studyManager;

    public Scheduler(ScheduleManager studyManager) {
        this.studyManager = studyManager;
    }

    @Scheduled(cron = "0 0 * * * *")
    void changeStudyAsInProgress() {
        progressStudies();
    }

    private void progressStudies() {
        studyManager.getAllStudies().studies()
            .forEach(studyId ->
                studyManager.updateStudyWithMembers(studyId, StudyStatus.IN_PROGRESS,
                    StudyMemberStatus.DENIED));
    }

}