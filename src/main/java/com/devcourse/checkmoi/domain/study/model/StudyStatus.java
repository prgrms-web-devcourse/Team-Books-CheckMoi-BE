package com.devcourse.checkmoi.domain.study.model;

import java.util.Arrays;
import java.util.Set;

public enum StudyStatus {
    RECRUITING("RECRUITING",
        Set.of(NextStatus.IN_PROGRESS, NextStatus.RECRUITING)),
    RECRUITING_FINISHED("RECRUITINGFINISHED",
        Set.of(NextStatus.IN_PROGRESS)),
    IN_PROGRESS("INPROGRESS",
        Set.of(NextStatus.RECRUITING, NextStatus.IN_PROGRESS, NextStatus.FINISHED)),
    FINISHED("FINISHED",
        Set.of(NextStatus.FINISHED));

    private final String name;

    private final Set<NextStatus> allowedNextStatus;

    StudyStatus(String name, Set<NextStatus> allowedNextStatus) {
        this.name = name;
        this.allowedNextStatus = allowedNextStatus;
    }

    public static StudyStatus nameOf(String name) {
        return Arrays.stream(values())
            .filter(value -> value.name.equals(name.toUpperCase()))
            .findFirst()
            .orElse(null);
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

    @Override
    public String toString() {
        return name;
    }
}
