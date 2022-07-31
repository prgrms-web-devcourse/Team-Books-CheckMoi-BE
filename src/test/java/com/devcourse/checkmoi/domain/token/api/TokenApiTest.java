package com.devcourse.checkmoi.domain.token.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.token.dto.TokenRequest;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

class TokenApiTest extends IntegrationTest {

    @Nested
    @DisplayName("토큰방식 인증을 통해 로그인을 유지한다 #15")
    class RefreshAccessToken {

        @Test
        @DisplayName("S refreshToken 을 사용해 새로운 accessToken 을 발급받는다")
        void refreshAccessToken() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            TokenRequest.RefreshToken request = TokenRequest.RefreshToken.builder()
                .refreshToken(givenUser.refreshToken())
                .build();

            mockMvc.perform(post("/api/tokens")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andDo(documentation());
        }

        @Test
        @DisplayName("F refreshToken 이 비어있으면 accessToken 을 재발급 받지 못한다")
        void emptyRefreshTokenRequest() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            TokenRequest.RefreshToken request = TokenRequest.RefreshToken.builder().build();

            mockMvc.perform(post("/api/tokens")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message")
                    .value("refreshToken은 비어있을 수 없습니다."))
                .andDo(print());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("token-refresh",
                ResourceSnippetParameters.builder()
                    .tag("Token API")
                    .summary("로그인 유지 API")
                    .description("토큰방식 인증을 통해 로그인을 유지하는 API 입니다.")
                    .requestSchema(Schema.schema("로그인 유지 요청"))
                    .responseSchema(Schema.schema("로그인 유지 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),

                requestFields(
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                        .description("리프레쉬 토큰")
                ),
                tokenRequestHeader(),
                responseFields(
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                        .description("재발급 된 액세스 토큰")
                )
            );
        }
    }

    @Nested
    @DisplayName("로그아웃을 할 수 있다 #15")
    class DeleteRefreshToken {

        @Test
        @DisplayName("S 저장된 refreshToken 을 폐기한다")
        void deleteRefreshToken() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/logout")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isNoContent())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("token-delete",
                ResourceSnippetParameters.builder()
                    .tag("Token API")
                    .summary("로그아웃 API")
                    .description("저장된 refreshToken 을 폐기합니다"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }

    }
}
