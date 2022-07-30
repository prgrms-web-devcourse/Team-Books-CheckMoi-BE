package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.converter.UserConverter;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.Register;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.security.oauth.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserConverter userConverter;

    private final UserRepository userRepository;

    @Transactional
    public Register register(UserProfile userProfile) {
        User user = userRepository.findByOauthId(String.valueOf(userProfile.getOauthId()))
            .orElseGet(() -> userRepository.save(userConverter.profileToUser(userProfile)));
        return userConverter.userToRegister(user);
    }

    public UserInfo findUserInfo(Long userId) {
        User findUser = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        return userConverter.userToUserInfo(findUser);
    }

    @Transactional
    public void deleteUserAccount(Long userId) {
        userRepository.deleteById(userId);
    }

}
