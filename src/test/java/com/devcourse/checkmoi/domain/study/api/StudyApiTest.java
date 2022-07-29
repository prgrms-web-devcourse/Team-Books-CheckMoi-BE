package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.util.DocumentUtil.getDateFormat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.service.study.StudyCommandService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.template.ApiTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(StudyApi.class)
class StudyApiTest extends ApiTest {

    @MockBean
    private StudyCommandService studyCommandService;

    @Nested
    @DisplayName("스터디 등록 (개설) #5")
    class CreateStudy {

        @Test
        @WithMockUser
        @DisplayName("S 스터디를 등록할 수 있다")
        void createStudy() throws Exception {
            StudyRequest.CreateStudy request = StudyRequest.CreateStudy.builder()
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

            when(studyCommandService.createStudy(any(StudyRequest.CreateStudy.class))).thenReturn(
                createdStudyId);
            MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/studies")
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
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
                .createStudy(any(StudyRequest.CreateStudy.class));
            assertThat(got.data()).isEqualTo(want.data());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-create",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 등록")
                    .description("스터디 등록에 사용되는 API입니다.")
                    .requestSchema(Schema.schema("StudyRequest.CreateStudy"))
                    .responseSchema(Schema.schema("Study ID")),
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
}