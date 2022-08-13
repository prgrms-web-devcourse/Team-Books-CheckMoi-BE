package com.devcourse.checkmoi.domain.user.repository;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfoWithStudy;

public interface CustomUserRepository {

    UserInfoWithStudy findUserInfoWithStudy(Long userId);

    int userJoinedStudies(Long userId);
}
