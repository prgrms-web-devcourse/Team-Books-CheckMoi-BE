package com.devcourse.checkmoi.domain.study.service.validator;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;

public interface StudyValidator {

    void validateExistStudy(boolean existStudy);

    void validateStudyOwner(Long userId, Long studyOwnerId, String message);

    void validateDuplicateStudyMemberRequest(StudyMember studyMember);

    void validateOngoingStudy(Study study);

    void validateParticipateUser(Long memberId);

    void validateRecruitingStudy(Study study);

    void validateFullMemberStudy(Study study);
}
