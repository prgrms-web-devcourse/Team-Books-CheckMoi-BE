package com.devcourse.checkmoi.domain.token.service;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import com.devcourse.checkmoi.domain.token.model.Token;
import com.devcourse.checkmoi.domain.token.repository.TokenRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Test
    @Transactional
    @DisplayName("저장되어있는 유저의 토큰을 삭제할 수 있다")
    void tokenRemoveTest() {
        User user = userRepository.save(makeUser());
        tokenRepository.saveAndFlush(
            Token.builder().userId(user.getId()).refreshToken("refresh").build());
        tokenRepository.deleteByUserId(user.getId());
    }

    @Test
    @Transactional
    @DisplayName("저장되어있지 않은 유저의 토큰을 지우더라도 에러가 나지 않는다")
    void tokenRemoveTest2() {
        User user = userRepository.save(makeUser());
        tokenRepository.saveAndFlush(
            Token.builder().userId(user.getId()).refreshToken("refresh").build());
        tokenRepository.deleteByUserId(20000L);
    }
}
