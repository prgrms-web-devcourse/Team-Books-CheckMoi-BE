package com.devcourse.checkmoi.global.scheduler;

import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final static String message = "[Scheduler] : ";

    private final ScheduleManager studyManager;

    public Scheduler(ScheduleManager studyManager) {
        this.studyManager = studyManager;
    }

    @Scheduled(cron = "0 0 0 * * *")
    void changeStudyAsInProgress() {
        log.info(message + " 스터디 상태 변경 작업 시작");

        try {
            progressStudies();
        } catch (Exception e) {
            log.error(message + "[ERROR] : 스터디를 진행중 상태로 변경하는 스케줄링 작업 실패", e);
        }

        log.info(message + " 스터디 상태 변경 작업 완료!!");
    }

    private void progressStudies() {
        studyManager.getAllStudiesToBeProgressed().studies()
            .forEach(studyId ->
                studyManager.updateStudyWithMembers(studyId, StudyStatus.IN_PROGRESS,
                    StudyMemberStatus.DENIED));
    }

}