package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;

public interface UserQueryService {

    UserInfo findUserInfo(Long userId);
}
