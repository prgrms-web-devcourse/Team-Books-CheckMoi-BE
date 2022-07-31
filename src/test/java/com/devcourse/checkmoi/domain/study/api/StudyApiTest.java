package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.util.DocumentUtil.getDateFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.service.study.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.study.StudyQueryService;
import com.devcourse.checkmoi.domain.study.stub.StudyStub;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.global.model.PageRequest;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class StudyApiTest extends IntegrationTest {

    @MockBean
    private StudyCommandService studyCommandService;

    @MockBean
    private StudyQueryService studyQueryService;

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
            Long userId = 1L;

            when(studyCommandService.createStudy(any(StudyRequest.Create.class), anyLong()))
                .thenReturn(createdStudyId);

            ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/studies")
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .content(toJson(request)));

            result.andExpect(status().isCreated())
                .andDo(documentation())
                .andExpect(jsonPath("$.data").value(createdStudyId));

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

    @Nested
    @DisplayName("특정 책에 대한 스터디 목록 조회 #43")
    class GetStudies {

        private final StudyConverter studyConverter = new StudyConverter();

        @Test
        @DisplayName("현재 모집중인 특정 책에 대한 스터디 목록을 조회한다.")
        void getStudies() throws Exception {
            Long bookId = 1L;
            PageRequest pageRequest = new PageRequest();
            Pageable pageable = pageRequest.of();
            Studies response = new Studies(
                new PageImpl<>(
                    StudyStub.javaRecrutingStudyStub())
                    .map(studyConverter::studyToStudyInfo)
            );
            given(studyQueryService.getStudies(anyLong(), any(Pageable.class)))
                .willReturn(response);

            ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/studies")
                    .param("bookId", String.valueOf(bookId))
                    .param("size", "2")
                    .param("page", "1")
            );

            result.andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            String dataPath = "data.studies.content[]";
            String pagePath = "data.studies";
            return MockMvcRestDocumentationWrapper.document("study-get-by-book",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("선택한 책의 모집중인 스터디 목록 확인")
                    .description("선택한 책의 모집중인 스터디 목록 확인하는 API 입니다.")
                    .responseSchema(Schema.schema("선택한 책의 모집중인 스터디 목록 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("bookId").description("책 ID"),
                    parameterWithName("size").description("가져올 스터디 수"),
                    parameterWithName("page").description("가져올 페이지")
                ),
                responseFields(
                    fieldWithPath(dataPath + ".id").type(JsonFieldType.NUMBER)
                        .description("스터디 ID"),
                    fieldWithPath(dataPath + ".name").type(JsonFieldType.STRING)
                        .description("스터디 이름"),
                    fieldWithPath(dataPath + ".thumbnailUrl").type(JsonFieldType.STRING)
                        .description("스터디 썸네일"),
                    fieldWithPath(dataPath + ".description").type(JsonFieldType.STRING)
                        .description("스터디 설명"),
                    fieldWithPath(dataPath + ".currentParticipant").type(
                        JsonFieldType.NUMBER).description("현재 스터디 참가 인원"),
                    fieldWithPath(dataPath + ".maxParticipant").type(JsonFieldType.NUMBER)
                        .description("최대 스터디 참가 인원"),
                    fieldWithPath(dataPath + ".gatherStartDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 시작 일자"),
                    fieldWithPath(dataPath + ".gatherEndDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 종료 일자"),
                    fieldWithPath(dataPath + ".studyStartDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 시작 일자"),
                    fieldWithPath(dataPath + ".studyEndDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 종료 일자"),
                    fieldWithPath(pagePath + ".pageable").type(JsonFieldType.STRING)
                        .description("Pageable"),
                    fieldWithPath(pagePath + ".last").type(JsonFieldType.BOOLEAN)
                        .description("마지막 페이지"),
                    fieldWithPath(pagePath + ".totalPages").type(JsonFieldType.NUMBER)
                        .description("총 페이지"),
                    fieldWithPath(pagePath + ".totalElements").type(JsonFieldType.NUMBER)
                        .description("총 스터디 수"),
                    fieldWithPath(pagePath + ".first").type(JsonFieldType.BOOLEAN)
                        .description("첫 페이지"),
                    fieldWithPath(pagePath + ".size").type(JsonFieldType.NUMBER)
                        .description("현재 페이지의 스터디 수"),
                    fieldWithPath(pagePath + ".number").type(JsonFieldType.NUMBER)
                        .description("스터디 수"),
                    fieldWithPath(pagePath + ".sort.empty").type(JsonFieldType.BOOLEAN)
                        .description("정렬 기준 값 존재 여부"),
                    fieldWithPath(pagePath + ".sort.sorted").type(JsonFieldType.BOOLEAN)
                        .description("정렬되었는지 여부"),
                    fieldWithPath(pagePath + ".sort.unsorted").type(JsonFieldType.BOOLEAN)
                        .description("정렬되지 않았는지 여부"),
                    fieldWithPath(pagePath + ".numberOfElements").type(JsonFieldType.NUMBER)
                        .description("스터디 수"),
                    fieldWithPath(pagePath + ".empty").type(JsonFieldType.BOOLEAN)
                        .description("값이 비어있는지 여부")
                )
            );
        }
    }
}
