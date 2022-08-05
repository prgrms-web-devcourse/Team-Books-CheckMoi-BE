package com.devcourse.checkmoi.domain.study.model;

import java.util.Set;

public enum StudyStatus {
    RECRUITING(
        Set.of(NextStatus.IN_PROGRESS, NextStatus.RECRUITING)),
    IN_PROGRESS(
        Set.of(NextStatus.RECRUITING, NextStatus.IN_PROGRESS, NextStatus.FINISHED)),
    FINISHED(
        Set.of(NextStatus.FINISHED));

    private final Set<NextStatus> allowedNextStatus;

    StudyStatus(Set<NextStatus> allowedNextStatus) {
        this.allowedNextStatus = allowedNextStatus;
    }

    public boolean isAllowedNextStatus(StudyStatus status) {
        return this.allowedNextStatus.contains(NextStatus.of(status));
    }

    private enum NextStatus {
        RECRUITING,
        IN_PROGRESS,
        FINISHED;

        public static NextStatus of(StudyStatus status) {
            return NextStatus.valueOf(status.name());
        }

    }
}
