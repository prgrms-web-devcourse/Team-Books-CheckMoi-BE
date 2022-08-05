package com.devcourse.checkmoi.domain.study.service.validator;

import com.devcourse.checkmoi.domain.study.model.StudyMember;

public interface StudyServiceValidator {

    void validateExistStudy(boolean existStudy);

    void validateStudyOwner(Long userId, Long studyOwnerId, String message);

    void validateDuplicateStudyMemberRequest(StudyMember studyMember);
}
