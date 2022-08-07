package com.devcourse.checkmoi.domain.comment.service.validator;

import com.devcourse.checkmoi.domain.study.model.StudyStatus;

public interface CommentServiceValidator {

    void finishedStudy(StudyStatus status);

    void existPost(boolean existPost);

    void existUser(boolean existPost);

    void joinStudyUser(boolean isJoinMember);
}
