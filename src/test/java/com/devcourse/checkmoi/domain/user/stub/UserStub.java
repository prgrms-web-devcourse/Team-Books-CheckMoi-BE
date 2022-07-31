package com.devcourse.checkmoi.domain.user.stub;

import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.UserRole;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import java.util.List;

public class UserStub {

    public static List<User> usersStub() {
        return List.of(
            User.builder()
                .id(1L)
                .oauthId("ASDASDQWDAASDZFWEF1")
                .provider("KAKAO")
                .name("user1")
                .email(new Email("user1@test.com"))
                .profileImgUrl("https://example.com/java.png")
                .userRole(UserRole.GUEST)
                .build(),
            User.builder()
                .id(2L)
                .oauthId("ASDASDQWDAASDZFWEF2")
                .provider("KAKAO")
                .name("user2")
                .email(new Email("user2@test.com"))
                .profileImgUrl("https://example.com/java2.png")
                .userRole(UserRole.GUEST)
                .build(),
            User.builder()
                .id(3L)
                .oauthId("ASDASDQWDAASDZFWEF3")
                .provider("KAKAO")
                .name("user3")
                .email(new Email("user3@test.com"))
                .profileImgUrl("https://example.com/java.png")
                .userRole(UserRole.HOST)
                .build(),
            User.builder()
                .id(4L)
                .oauthId("ASDASDQWDAASDZFWEF4")
                .provider("KAKAO")
                .name("user4")
                .email(new Email("user4@test.com"))
                .profileImgUrl("https://example.com/java.png")
                .userRole(UserRole.GUEST)
                .build(),
            User.builder()
                .id(5L)
                .oauthId("ASDASDQWDAASDZFWEF5")
                .provider("KAKAO")
                .name("user5")
                .email(new Email("user5@test.com"))
                .profileImgUrl("https://example.com/java5.png")
                .userRole(UserRole.HOST)
                .build()
        );
    }
}
