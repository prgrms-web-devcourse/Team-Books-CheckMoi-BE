package com.devcourse.checkmoi.domain.study.model;

import java.util.Collections;
import java.util.Set;

public enum StudyStatus {
    RECRUITING(
        Set.of(NextStatus.IN_PROGRESS, NextStatus.RECRUITING_FINISHED)),
    RECRUITING_FINISHED(
        Set.of(NextStatus.IN_PROGRESS)),
    IN_PROGRESS(
        Set.of(NextStatus.FINISHED)),
    FINISHED(
        Collections.emptySet());

    private final Set<NextStatus> allowedNextStatus;

    StudyStatus(Set<NextStatus> allowedNextStatus) {
        this.allowedNextStatus = allowedNextStatus;
    }

    public boolean isAllowedToChangeStatus(StudyStatus status) {
        return this.isUnchanged(status) ||
            this.isAllowedNextStatus(status);
    }

    private boolean isUnchanged(StudyStatus status) {
        return this == status;
    }

    private boolean isAllowedNextStatus(StudyStatus status) {
        return !this.allowedNextStatus.isEmpty() &&
            this.allowedNextStatus.contains(NextStatus.of(status));
    }

    private enum NextStatus {
        RECRUITING,
        RECRUITING_FINISHED,
        IN_PROGRESS,
        FINISHED;

        public static NextStatus of(StudyStatus status) {
            return NextStatus.valueOf(status.name());
        }

    }
}
