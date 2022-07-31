package com.devcourse.checkmoi.domain.user.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.user.dto.UserRequest;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

class UserIntegrationTest extends IntegrationTest {


    @Nested
    @DisplayName("회원가입/로그인 #10")
    class OAuth2Login {

        @ParameterizedTest
        @DisplayName("S oauth 로그인 요청 시 리다이렉트")
        @ValueSource(strings = "kakao")
        void loginDocument(String vendor) throws Exception {
            mockMvc.perform(post("/oauth2/authorization/" + vendor))
                .andExpect(status().is3xxRedirection());
        }
    }

    @Nested
    @DisplayName("회원정보를 조회할 수 있다 #4")
    class UserPage {

        @Test
        @DisplayName("S 회원 상세 페이지 조회")
        void userPage() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(get("/api/users/{userId}", givenUser.userInfo().id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id")
                    .value(givenUser.userInfo().id()))
                .andExpect(jsonPath("$.data.name")
                    .value(givenUser.userInfo().name()))
                .andExpect(jsonPath("$.data.email")
                    .value(givenUser.userInfo().email()))
                .andExpect(jsonPath("$.data.profileImageUrl")
                    .value(givenUser.userInfo().profileImageUrl()))
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("user-detail",
                ResourceSnippetParameters.builder()
                    .tag("User API")
                    .summary("회원 정보 조회 API")
                    .description("회원 정보를 요청하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("userId").description("회원 아이디")
                ),
                tokenRequestHeader()
            );
        }
    }

    @Nested
    @DisplayName("회원 서비스 탈퇴 #4")
    class DeleteAccount {

        @Test
        @DisplayName("S 서비스 탈퇴")
        void deleteUser() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(delete("/api/users/{userId}", givenUser.userInfo().id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isNoContent())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("user-exit",
                ResourceSnippetParameters.builder()
                    .tag("User API")
                    .summary("회원 탈퇴 API")
                    .description("서비스 탈퇴를 요청하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("userId").description("회원 아이디")
                )
            );

        }
    }

    @Nested
    @DisplayName("유저 정보를 수정할 수 있다 #4")
    class EditAccount {

        @Test
        @DisplayName("S 유저 정보를 수정할 수 있다")
        void editAccount() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            UserRequest.Edit request = UserRequest.Edit.builder()
                .name("수정된 유저 이름")
                .profileImageUrl("수정된 유저 프로필 이미지")
                .build();

            mockMvc.perform(put("/api/users/{userId}", givenUser.userInfo().id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(givenUser.userInfo().id()))
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("user-edit",
                ResourceSnippetParameters.builder()
                    .tag("User API")
                    .summary("유저 수정 API")
                    .description("유저 정보를 수정하는 API 입니다.")
                    .requestSchema(Schema.schema("유저 수정 요청"))
                    .responseSchema(Schema.schema("유저 수정 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),

                pathParameters(
                    parameterWithName("userId").description("회원 아이디")
                ),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("수정할 유저 이름"),
                    fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                        .description("수정할 유저 프로필 이미지 URL")
                ),
                tokenRequestHeader(),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("수정 된 유저 아이디")
                )
            );

        }
    }

}
