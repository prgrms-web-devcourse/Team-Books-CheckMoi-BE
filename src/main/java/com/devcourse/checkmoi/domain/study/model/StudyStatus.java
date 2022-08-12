package com.devcourse.checkmoi.domain.study.model;

import com.devcourse.checkmoi.global.annotation.CodeMappable;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum StudyStatus implements CodeMappable {
    RECRUITING("recruiting",
        Set.of(NextStatus.IN_PROGRESS, NextStatus.RECRUITING_FINISHED)),
    RECRUITING_FINISHED("recruitingFinished",
        Set.of(NextStatus.IN_PROGRESS)),
    IN_PROGRESS("inProgress",
        Set.of(NextStatus.FINISHED)),
    FINISHED("finished",
        Collections.emptySet());

    private static final Map<String, StudyStatus> strToStudyStatus = new HashMap<>();

    static {
        strToStudyStatus.put(RECRUITING.getMappingCode(), RECRUITING);
        strToStudyStatus.put(RECRUITING_FINISHED.getMappingCode(), RECRUITING_FINISHED);
        strToStudyStatus.put(IN_PROGRESS.getMappingCode(), IN_PROGRESS);
        strToStudyStatus.put(FINISHED.getMappingCode(), FINISHED);
    }

    private final String code;

    private final Set<NextStatus> allowedNextStatus;

    StudyStatus(String code, Set<NextStatus> allowedNextStatus) {
        this.code = code;
        this.allowedNextStatus = allowedNextStatus;
    }

    public static StudyStatus nameOf(String inputCode) {
        return strToStudyStatus.getOrDefault(inputCode, null);
    }

    @JsonValue
    @Override
    public String getMappingCode() {
        return this.code;
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
