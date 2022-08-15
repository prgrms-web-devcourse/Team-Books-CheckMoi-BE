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

    @Scheduled(cron = "0 0 * * * *")
    public void changeStudyAsInProgress() {
        log.info(message + " 스터디 상태 변경 작업 (-> 진행 중 ) 시작");

        try {
            progressStudies();
        } catch (Exception e) {
            log.error(message + "[ERROR] : 스터디를 진행중 상태로 변경하는 스케줄링 작업 실패", e);
        }

        log.info(message + " 스터디 상태 변경 작업 완료!!");
    }

    @Scheduled(cron = "0 0 * * * *")
    public void changeStudyAsFinished() {
        log.info(message + " 스터디 상태 변경 작업 (-> 진행 완료 ) 시작");

        try {
            completeStudies();
        } catch (Exception e) {
            log.error(message + "[ERROR] : 스터디를 진행완료 상태로 변경하는 스케줄링 작업 실패", e);
        }

        log.info(message + " 스터디 상태 변경 작업 완료!!");
    }

    private void progressStudies() {
        studyManager.getAllStudiesToBeProcessed(StudyStatus.IN_PROGRESS).studies()
            .forEach(studyId -> this.updateStudyWithMembers(studyId, StudyStatus.IN_PROGRESS,
                StudyMemberStatus.DENIED));
    }

    private void completeStudies() {
        studyManager.getAllStudiesToBeProcessed(StudyStatus.FINISHED).studies()
            .forEach(studyId -> this.updateStudy(studyId, StudyStatus.FINISHED));
    }

    private void updateStudyWithMembers(Long studyId, StudyStatus studyStatus,
        StudyMemberStatus memberStatus) {

        try {
            studyManager.updateStudyWithMembers(studyId, studyStatus, memberStatus);
        } catch (Exception e) {
            log.error(
                message + "[ERROR] : 스터디를 진행중 상태로 변경하는 스케줄링 작업 실패 - 실패한 studyId : " + studyId);
        }
    }

    private void updateStudy(Long studyId, StudyStatus studyStatus) {
        try {
            studyManager.updateStudy(studyId, studyStatus);
        } catch (Exception e) {
            log.error(
                message + "[ERROR] : 스터디를 진행완료 상태로 변경하는 스케줄링 작업 실패 - 실패한 studyId : " + studyId);
        }
    }

}
