package com.devcourse.checkmoi.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceImplTest {

    @InjectMocks
    UserQueryServiceImpl userQueryService;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("유저의 스터디 참여 수 확인 #223")
    class UserJoinedStudiesTest {

        @Test
        @DisplayName("유저의 스터디 참여 수 확인")
        void userJoinedStudies() {
            Long userId = 1L;
            int want = 1;
            given(userRepository.userJoinedStudies(userId))
                .willReturn(want);

            int got = userQueryService.getUserJoinedStudies(userId);

            assertThat(got).isEqualTo(want);
        }
    }
 }