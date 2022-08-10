package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.dto.UserRequest.Edit;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.global.security.oauth.UserProfile;

public interface UserCommandService {

    Register registerAccount(UserProfile userProfile);

    void deleteUserAccount(Long userId, Long authId);

    void editAccount(Long userId, Long authId, Edit request);
}
