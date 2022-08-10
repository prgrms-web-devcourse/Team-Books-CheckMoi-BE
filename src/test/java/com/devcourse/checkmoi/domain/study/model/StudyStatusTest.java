package com.devcourse.checkmoi.domain.study.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudyStatusTest {

    @Test
    @DisplayName("F 모집중이던 스터디를 진행완료로 변경할 수 없다")
    void changeStatusToFinishedFail() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.FINISHED);

        Assertions.assertThat(finished)
            .isFalse();
    }

    @Test
    @DisplayName("S 모집중이던 스터디를 진행중으로 변경할 수 있다")
    void changeStatusToInProgress() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.IN_PROGRESS);

        Assertions.assertThat(finished)
            .isTrue();
    }

    @Test
    @DisplayName("S 모집중이던 스터디를 모집마감으로 변경할 수 있다")
    void changeStatusToRecruitingFinished() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.RECRUITING_FINISHED);

        Assertions.assertThat(finished)
            .isTrue();
    }

    @Test
    @DisplayName("F 모집 마감 스터디를 모집 중으로 변경할 수 없다")
    void changeRecruitingFinishedToRecruitingFail() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING_FINISHED;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.RECRUITING);

        Assertions.assertThat(finished)
            .isFalse();
    }

    @Test
    @DisplayName("S 모집 마감 스터디를 진행 중으로 변경할 수 있다")
    void changeRecruitingFinishedToInProgress() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING_FINISHED;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.IN_PROGRESS);

        Assertions.assertThat(finished)
            .isTrue();
    }

    @Test
    @DisplayName("F 모집 마감 스터디를 진행 완료로 변경할 수 없다")
    void changeRecruitingFinishedToFinishedFail() {
        StudyStatus beforeStatus = StudyStatus.RECRUITING_FINISHED;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.FINISHED);

        Assertions.assertThat(finished)
            .isFalse();
    }

    @Test
    @DisplayName("F 진행중이던 스터디를 모집 중으로 변경할 수 없다")
    void changeStatusToRecruiting() {
        StudyStatus beforeStatus = StudyStatus.IN_PROGRESS;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.RECRUITING);

        Assertions.assertThat(finished)
            .isFalse();
    }

    @Test
    @DisplayName("F 진행중이던 스터디를 모집 마감으로 변경할 수 없다")
    void changeInProgressToRecruiting() {
        StudyStatus beforeStatus = StudyStatus.IN_PROGRESS;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.RECRUITING_FINISHED);

        Assertions.assertThat(finished)
            .isFalse();
    }

    @Test
    @DisplayName("S 진행중이던 스터디를 진행 완료로 변경할 수 있다")
    void changeInProgressToFinished() {
        StudyStatus beforeStatus = StudyStatus.IN_PROGRESS;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.FINISHED);

        Assertions.assertThat(finished)
            .isTrue();
    }

    @Test
    @DisplayName("F 진행 완료된 스터디를 진행 중으로 변경할 수 없다")
    void changeStatusToInProgressFail() {
        StudyStatus beforeStatus = StudyStatus.FINISHED;

        boolean finished = beforeStatus.isAllowedToChangeStatus(StudyStatus.IN_PROGRESS);

        Assertions.assertThat(finished)
            .isFalse();
    }

}