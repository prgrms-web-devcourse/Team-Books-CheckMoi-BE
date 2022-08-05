package com.devcourse.checkmoi.domain.study.model;

import com.devcourse.checkmoi.domain.study.exception.NotAllowedStudyStatusException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudyTest {

    @Nested
    class ChangeStatusTest {

        @Test
        @DisplayName("F 진행완료된 상태를 진행 중으로 변경할 수 없다 ")
        void changeStatusToInProgressFail() {
            Study study = Study.builder()
                .status(StudyStatus.FINISHED)
                .build();

            Assertions.assertThatThrownBy(() ->
                study.changeStatus(StudyStatus.IN_PROGRESS)
            ).isInstanceOf(NotAllowedStudyStatusException.class);
        }

        @Test
        @DisplayName("F 모집중 상태를 완료된 상태로 변경할 수 없다 ")
        void changeStatusToFinishedFail() {
            Study study = Study.builder()
                .status(StudyStatus.RECRUITING)
                .build();

            Assertions.assertThatThrownBy(() ->
                study.changeStatus(StudyStatus.FINISHED)
            ).isInstanceOf(NotAllowedStudyStatusException.class);
        }
    }
}