package com.devcourse.checkmoi.domain.study.api;

import com.devcourse.checkmoi.global.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleApi {

    private final Scheduler scheduler;

    @PatchMapping("/in-progress")
    public void changeToInProgress() {
        scheduler.changeStudyAsInProgress();
    }

    @PatchMapping("/finished")
    public void changeToFinished() {
        scheduler.changeStudyAsFinished();
    }
}
