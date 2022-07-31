package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.converter.UserConverter;
import com.devcourse.checkmoi.domain.user.dto.UserRequest.Edit;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.exception.UserNoPermissionException;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.security.oauth.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserConverter userConverter;

    private final UserRepository userRepository;

    @Override
    public Register registerAccount(UserProfile userProfile) {
        User user = userRepository.findByOauthId(String.valueOf(userProfile.getOauthId()))
            .orElseGet(() -> userRepository.save(userConverter.profileToUser(userProfile)));
        return userConverter.userToRegister(user);
    }

    @Override
    public void deleteUserAccount(Long userId, Long authId) {
        validatePermission(userId, authId, "서비스 탈퇴");

        userRepository.deleteById(userId);
    }

    @Override
    public void editAccount(Long userId, Long authId, Edit request) {
        validatePermission(userId, authId, "수정");

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        user.editName(request.name());
        user.editProfileImage(request.profileImageUrl());
    }

    private void validatePermission(Long userId, Long authId, String situation) {
        if (userId != authId) {
            throw new UserNoPermissionException(situation + " 작업에 대한 권한이 없습니다");
        }
    }
}
