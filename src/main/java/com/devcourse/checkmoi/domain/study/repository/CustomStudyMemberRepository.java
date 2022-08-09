package com.devcourse.checkmoi.domain.study.repository;

public interface CustomStudyMemberRepository {

    Long participateUserInStudy(Long studyId, Long userId);

}
