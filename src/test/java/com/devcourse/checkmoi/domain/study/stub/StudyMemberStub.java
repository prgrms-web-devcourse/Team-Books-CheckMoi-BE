package com.devcourse.checkmoi.domain.study.stub;

import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.stub.UserStub;
import java.util.List;

public class StudyMemberStub {

    private static final List<User> users = UserStub.usersStub();
    private static final List<Study> studies = StudyStub.studiesStub();

    public static List<StudyMember> studyMembers() {
        return List.of(
            StudyMember.builder()
                .id(1L)
                .status(StudyMemberStatus.ACCEPTED)
                .user(users.get(0))
                .study(studies.get(0))
                .build(),
            StudyMember.builder()
                .id(2L)
                .status(StudyMemberStatus.ACCEPTED)
                .user(users.get(1))
                .study(studies.get(0))
                .build(),
            StudyMember.builder()
                .id(3L)
                .status(StudyMemberStatus.ACCEPTED)
                .user(users.get(2))
                .study(studies.get(0))
                .build(),
            StudyMember.builder()
                .id(4L)
                .status(StudyMemberStatus.ACCEPTED)
                .user(users.get(3))
                .study(studies.get(2))
                .build(),
            StudyMember.builder()
                .id(5L)
                .status(StudyMemberStatus.ACCEPTED)
                .user(users.get(4))
                .study(studies.get(2))
                .build()
        );
    }

    public static StudyMember studyMember() {
        return StudyMember.builder()
            .id(1L)
            .status(StudyMemberStatus.OWNED)
            .user(users.get(0))
            .study(studies.get(0))
            .build();
    }
}
