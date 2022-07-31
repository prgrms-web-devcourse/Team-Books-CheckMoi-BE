package com.devcourse.checkmoi.domain.user.service;

import com.devcourse.checkmoi.domain.user.converter.UserConverter;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserConverter userConverter;

    private final UserRepository userRepository;

    public UserInfo findUserInfo(Long userId) {
        User findUser = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        return userConverter.userToUserInfo(findUser);
    }

}
