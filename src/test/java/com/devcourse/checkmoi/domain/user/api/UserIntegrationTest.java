package com.devcourse.checkmoi.domain.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.template.IntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;

class UserIntegrationTest extends IntegrationTest {

    @ParameterizedTest
    @DisplayName("oauth 로그인 요청 시 리다이렉트")
    @ValueSource(strings = "kakao")
    void loginDocument(String vendor) throws Exception {
        mockMvc.perform(post("/oauth2/authorization/" + vendor))
            .andExpect(status().is3xxRedirection());
    }

    @Nested
    @DisplayName("유저 정보 조회")
    class UserPage {

        @Test
        void userPage() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(get("/api/users/{userId}", givenUser.userInfo().id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("유저 서비스 탈퇴")
    class DeleteAccount {

        @Test
        void deleteUser() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(delete("/api/users/{userId}", givenUser.userInfo().id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isNoContent())
                .andDo(print());
        }
    }
}
