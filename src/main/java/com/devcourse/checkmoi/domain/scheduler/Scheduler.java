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
        try {
            progressStudies();
        } catch (Exception e) {
            log.error("[ERROR] : 스터디를 진행중 상태로 변경하는 스케줄링 작업 실패", e);
        }
    }

    private void progressStudies() {
        studyManager.getAllStudies().studies()
            .forEach(studyId ->
                studyManager.updateStudyWithMembers(studyId, StudyStatus.IN_PROGRESS,
                    StudyMemberStatus.DENIED));
    }

}