package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfoWithStudy;

public interface UserQueryService {

    UserInfo findUserInfo(Long userId);

    UserInfoWithStudy findUserInfoWithStudy(Long id);

    int userJoinedStudies(Long userId);
}
