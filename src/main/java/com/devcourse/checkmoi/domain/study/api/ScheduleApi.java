package com.devcourse.checkmoi.domain.study.api;

import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.global.scheduler.ScheduleManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleApi {

    private final static String message = "[Scheduler API] : ";

    private final ScheduleManager scheduleService;

    @GetMapping("/in-progress")
    public void changeToInProgress() {
        log.info(message + " 스터디 상태 변경 작업 (-> 진행 중 ) 시작");

        try {
            progressStudies();
        } catch (Exception e) {
            log.error(message + "[ERROR] : 스터디를 진행중 상태로 변경하는 스케줄링 작업 실패", e);
        }

        log.info(message + " 스터디 상태 변경 작업 완료!!");
    }

    @GetMapping("/finished")
    public void changeToFinished() {
        log.info(message + " 스터디 상태 변경 작업 (-> 진행 완료 ) 시작");

        try {
            completeStudies();
        } catch (Exception e) {
            log.error(message + "[ERROR] : 스터디를 진행완료 상태로 변경하는 스케줄링 작업 실패", e);
        }

        log.info(message + " 스터디 상태 변경 작업 완료!!");
    }

    private void progressStudies() {
        scheduleService.getAllStudiesToBeProcessed(StudyStatus.IN_PROGRESS).studies()
            .forEach(studyId -> updateStudyWithMembers(studyId, StudyStatus.IN_PROGRESS,
                StudyMemberStatus.DENIED));
    }

    private void completeStudies() {
        scheduleService.getAllStudiesToBeProcessed(StudyStatus.FINISHED).studies()
            .forEach(studyId -> updateStudy(studyId, StudyStatus.FINISHED));
    }

    private void updateStudyWithMembers(Long studyId, StudyStatus studyStatus,
        StudyMemberStatus memberStatus) {
        try {
            scheduleService.updateStudyWithMembers(studyId, studyStatus, memberStatus);
        } catch (Exception e) {
            log.error(
                message + "[ERROR] : 스터디를 진행중 상태로 변경하는 스케줄링 작업 실패 - 실패한 studyId : " + studyId);
        }
    }

    private void updateStudy(Long studyId, StudyStatus studyStatus) {
        try {
            scheduleService.updateStudy(studyId, studyStatus);
        } catch (Exception e) {
            log.error(
                message + "[ERROR] : 스터디를 진행완료 상태로 변경하는 스케줄링 작업 실패 - 실패한 studyId : " + studyId);
        }
    }
}
