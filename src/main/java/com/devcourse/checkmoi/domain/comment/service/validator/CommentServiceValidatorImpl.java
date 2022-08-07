package com.devcourse.checkmoi.domain.comment.service.validator;

import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinRequestNotFoundException;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CommentServiceValidatorImpl implements
    CommentServiceValidator {

    @Override
    public void finishedStudy(StudyStatus status) {
        if (status == StudyStatus.FINISHED) {
            throw new FinishedStudyException();
        }
    }

    @Override
    public void existPost(boolean existPost) {
        if (!existPost) {
            throw new PostNotFoundException();
        }
    }

    @Override
    public void existUser(boolean existUser) {
        if (!existUser) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void joinStudyUser(boolean isJoinMember) {
        if (!isJoinMember) {
            throw new StudyJoinRequestNotFoundException();
        }
    }
}
