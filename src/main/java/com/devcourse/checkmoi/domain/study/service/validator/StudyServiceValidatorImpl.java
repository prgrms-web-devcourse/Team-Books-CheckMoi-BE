package com.devcourse.checkmoi.domain.study.service.validator;

import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.ACCESS_DENIED;
import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.STUDY_JOIN_REQUEST_DUPLICATE;
import com.devcourse.checkmoi.domain.study.exception.DuplicateStudyJoinRequestException;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotParticipateStudyUserException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import org.springframework.stereotype.Component;

@Component
public class StudyServiceValidatorImpl implements StudyServiceValidator {

    @Override
    public void validateExistStudy(boolean existStudy) {
        if (!existStudy) {
            throw new StudyNotFoundException();
        }
    }

    @Override
    public void validateStudyOwner(Long userId, Long studyOwnerId, String message) {
        if (!studyOwnerId.equals(userId)) {
            throw new NotStudyOwnerException(message, ACCESS_DENIED);
        }
    }

    @Override
    public void validateDuplicateStudyMemberRequest(StudyMember studyMember) {
        if (studyMember.getStatus() != StudyMemberStatus.DENIED) {
            throw new DuplicateStudyJoinRequestException(STUDY_JOIN_REQUEST_DUPLICATE);
        }
    }

    @Override
    public void ongoingStudy(Study study) {
        if (study.getStatus() == StudyStatus.FINISHED) {
            throw new FinishedStudyException();
        }
    }

    @Override
    public void participateUser(Long memberId) {
        if (memberId == null) {
            throw new NotParticipateStudyUserException();
        }
    }


}
