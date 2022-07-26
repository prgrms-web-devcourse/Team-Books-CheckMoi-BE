package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.dto.response.MyUserInfoResponse;
import com.devcourse.checkmoi.domain.user.dto.response.UserRegisterResponse;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.security.oauth.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserRegisterResponse register(UserProfile userProfile) {
        var user = userRepository.findByOauthId(String.valueOf(userProfile.getOauthId()))
            .orElseGet(() -> userRepository.save(userProfile.toUser()));
        return new UserRegisterResponse(user.getId(), user.getUserRole().getGrantedAuthority());
    }

    public MyUserInfoResponse findUserInfo(Long userId) {
        var findUser = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        return MyUserInfoResponse.from(findUser);
    }

    @Transactional
    public void deleteUserAccount(Long userId) {
        userRepository.deleteById(userId);
    }

}
