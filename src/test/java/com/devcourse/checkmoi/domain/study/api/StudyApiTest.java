package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.util.DocumentUtil.getDateFormat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.service.study.StudyCommandService;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class StudyApiTest extends IntegrationTest {

    @MockBean
    private StudyCommandService studyCommandService;

    @Nested
    @DisplayName("스터디 등록")
    class CreateTest {

        @Test
        @DisplayName("S 스터디를 등록할 수 있다")
        void createStudy() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            StudyRequest.Create request = StudyRequest.Create.builder()
                .bookId(1L)
                .name("스터디 이름")
                .thumbnail("스터디 썸네일 URL")
                .description("스터디입니다")
                .maxParticipant(5)
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .build();
            Long createdStudyId = 1L;
            SuccessResponse<Long> want = new SuccessResponse<>(1L);

            when(studyCommandService.createStudy(any(StudyRequest.Create.class))).thenReturn(
                createdStudyId);

            MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/studies")
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .content(toJson(request)))
                .andExpect(status().isCreated())
                .andDo(documentation())
                .andReturn();

            SuccessResponse<Long> got = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
            );
            then(studyCommandService).should(times(1))
                .createStudy(any(StudyRequest.Create.class));
            assertThat(got.data()).isEqualTo(want.data());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-create",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 등록")
                    .description("스터디 등록에 사용되는 API입니다.")
                    .requestSchema(Schema.schema("스터디 생성 요청"))
                    .responseSchema(Schema.schema("스터디 생성 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("bookId").type(JsonFieldType.NUMBER)
                        .description("책 아이디"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("스터디 이름"),
                    fieldWithPath("thumbnail").type(JsonFieldType.STRING)
                        .description("스터디 썸네일"),
                    fieldWithPath("description").type(JsonFieldType.STRING)
                        .description("스터디 설명"),
                    fieldWithPath("maxParticipant").type(JsonFieldType.NUMBER)
                        .description("스터디 최대 참여 인원"),
                    fieldWithPath("gatherStartDate").type(JsonFieldType.STRING)
                        .attributes(getDateFormat())
                        .description("모집 시작일자"),
                    fieldWithPath("gatherEndDate").type(JsonFieldType.STRING)
                        .attributes(getDateFormat())
                        .description("모집 종료일자")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER)
                        .description("생성된 스터디 아이디")
                )
            );
        }
    }

    @Nested
    @DisplayName("스터디 수정")
    class EditTest {

        @Test
        @DisplayName("S 스터디를 수정할 수 있다.")
        void editStudyInfo() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            StudyRequest.Edit request = StudyRequest.Edit.builder()
                .name("스터디 이름")
                .thumbnail("https://example.com")
                .description("스터디 설명")
                .build();
            Long requestId = 1L;

            when(studyCommandService.editStudyInfo(anyLong(), anyLong(),
                any(StudyRequest.Edit.class)))
                .thenReturn(requestId);

            ResultActions result = mockMvc.perform(put("/api/studies/{id}", requestId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(toJson(request)));

            result.andExpect(status().isOk())
                .andDo(documentation())
                .andExpect(jsonPath("$.data").value(requestId));
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-edit",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 수정")
                    .description("스터디 수정에 사용되는 API입니다.")
                    .requestSchema(Schema.schema("스터디 수정 요청"))
                    .responseSchema(Schema.schema("스터디 수정 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("스터디 이름"),
                    fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("스터디 대표 이미지"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("스터디 설명")
                ),
                pathParameters(
                    parameterWithName("id").description("스터디 ID")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("스터디 ID")
                )
            );
        }
    }

    @Nested
    @DisplayName("스터디 가입 승낙 및 거절 #37")
    class Audit {

        @Test
        void auditStudyParticipation() throws Exception {
            Long studyId = 1L;
            Long memberId = 1L;
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();

            ResultActions result = mockMvc.perform(
                put("/api/studies/{studyId}/member/{memberId}", studyId, memberId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .content(toJson(request)));

            result.andExpect(status().isNoContent())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-audit",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 가입 승낙 및 거절")
                    .description("스터디 가입 승낙 및 거절에 사용되는 API입니다.")
                    .requestSchema(Schema.schema("스터디 가입 승낙 및 거절 요청")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("studyId").description("스터디 Id"),
                    parameterWithName("memberId").description("멤버 Id")
                ),
                requestFields(
                    fieldWithPath("status").description("스터디 상태")
                ));
        }
    }
}
