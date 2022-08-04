package com.devcourse.checkmoi.domain.study.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudyStatusTest {

    @Test
    @DisplayName("F 모집중이던 스터디를 진행완료로 변경할 수 없다")
    void changeStatusToFinishedFail() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING;

        boolean finished = beforeStatus.isAllowedNextStatus(StudyStatus.FINISHED);

        Assertions.assertThat(finished)
            .isFalse();
    }

    @Test
    @DisplayName("S 모집중이던 스터디를 진행중으로 변경할 수 있다")
    void changeStatusToInProgress() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING;

        boolean finished = beforeStatus.isAllowedNextStatus(StudyStatus.IN_PROGRESS);

        Assertions.assertThat(finished)
            .isTrue();
    }

    @Test
    @DisplayName("S 진행중이던 스터디를 모집 중으로 변경할 수 있다")
    void changeStatusToRecruiting() {
        StudyStatus beforeStatus = StudyStatus.IN_PROGRESS;

        boolean finished = beforeStatus.isAllowedNextStatus(StudyStatus.RECRUITING);

        Assertions.assertThat(finished)
            .isTrue();
    }

    @Test
    @DisplayName("F 진행 완료된 스터디를 진행 중으로 변경할 수 없다")
    void changeStatusToInProgressFail() {
        StudyStatus beforeStatus = StudyStatus.FINISHED;

        boolean finished = beforeStatus.isAllowedNextStatus(StudyStatus.IN_PROGRESS);

        Assertions.assertThat(finished)
            .isFalse();
    }

}