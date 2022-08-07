package com.devcourse.checkmoi.util;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import java.util.List;

public abstract class DTOGeneratorUtil {
    public static StudyInfo makeStudyInfo(Study study) {
        return StudyInfo.builder()
            .id(study.getId())
            .name(study.getName())
            .thumbnailUrl(study.getThumbnailUrl())
            .description(study.getDescription())
            .currentParticipant(1)
            .maxParticipant(study.getMaxParticipant())
            .gatherStartDate(study.getGatherStartDate())
            .gatherEndDate(study.getGatherEndDate())
            .studyStartDate(study.getStudyStartDate())
            .studyEndDate(study.getStudyEndDate())
            .build();

    }

    public static UserInfo makeUserInfo() {
        User user = makeUserWithId(1L);
        return UserInfo.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail().getValue())
            .profileImageUrl(user.getProfileImgUrl())
            .temperature(user.getTemperature())
            .build();
    }

    public static List<Studies> makeMyStudies() {
        return List.of(
            new Studies(
                List.of(makeStudyInfo(makeStudyWithId(makeBook(), StudyStatus.IN_PROGRESS, 1L)))
            ),
            new Studies(
                List.of(makeStudyInfo(makeStudyWithId(makeBook(), StudyStatus.FINISHED, 2L)))
            ),
            new Studies(
                List.of(makeStudyInfo(makeStudyWithId(makeBook(), StudyStatus.RECRUITING, 3L)))
            )
        );
    }
}