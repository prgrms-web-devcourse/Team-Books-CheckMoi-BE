package com.devcourse.checkmoi.domain.study.service.validator;

import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.ACCESS_DENIED;
import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.STUDY_JOIN_REQUEST_DUPLICATE;
import com.devcourse.checkmoi.domain.study.exception.DuplicateStudyJoinRequestException;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.exception.NotRecruitingStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinMaximumReachedException;
import com.devcourse.checkmoi.domain.study.exception.StudyMemberFullException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import org.springframework.stereotype.Component;

@Component
public class StudyValidatorImpl implements StudyValidator {

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
    public void validateOngoingStudy(Study study) {
        if (study.isFinished()) {
            throw new FinishedStudyException();
        }
    }

    @Override
    public void validateParticipateUser(Long memberId) {
        if (memberId == null) {
            throw new NotJoinedMemberException();
        }
    }

    @Override
    public void validateRecruitingStudy(Study study) {
        if (!study.isRecruiting()) {
            throw new NotRecruitingStudyException();
        }
    }

    @Override
    public void validateFullMemberStudy(Study study) {
        if (study.getCurrentParticipant() >= study.getMaxParticipant()) {
            throw new StudyMemberFullException();
        }
    }

    @Override
    public void validateMaximumJoinStudy(int joinStudy) {
        final int JOIN_MAX_STUDY = 10;
        if (joinStudy >= JOIN_MAX_STUDY) {
            throw new StudyJoinMaximumReachedException();
        }
    }
}
