package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeMyStudies;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeUserInfo;
import static com.devcourse.checkmoi.util.DocumentUtil.getDateFormat;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.facade.StudyUserFacade;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.global.model.PageRequest;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private StudyUserFacade studyUserFacade;

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
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build();
            Long createdStudyId = 1L;

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
                tokenRequestHeader(),
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
                        .description("모집 종료일자"),
                    fieldWithPath("studyStartDate").type(JsonFieldType.STRING)
                        .attributes(getDateFormat())
                        .description("스터디 시작 일자"),
                    fieldWithPath("studyEndDate").type(JsonFieldType.STRING)
                        .description("스터디 종료 일자")
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
                .status("IN_PROGRESS")
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
                tokenRequestHeader(),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("스터디 이름"),
                    fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("스터디 대표 이미지"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("스터디 설명"),
                    fieldWithPath("status").type(JsonFieldType.STRING).description("스터디 상태")
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
    class AuditTest {

        @Test
        void auditStudyParticipation() throws Exception {
            Long studyId = 1L;
            Long memberId = 1L;
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            StudyRequest.Audit request = StudyRequest.Audit.builder()
                .status("ACCEPTED")
                .build();

            ResultActions result = mockMvc.perform(
                put("/api/studies/{studyId}/members/{memberId}", studyId, memberId)
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
                tokenRequestHeader(),
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
    class GetStudiesTest {

        private final StudyConverter studyConverter = new StudyConverter();

        @Test
        @DisplayName("현재 모집중인 특정 책에 대한 스터디 목록을 조회한다.")
        void getStudies() throws Exception {
            Long bookId = 1L;
            PageRequest pageRequest = new PageRequest();
            Pageable pageable = pageRequest.of();

            Studies response = new Studies(
                List.of(
                        makeStudyWithId(makeBookWithId(1L), StudyStatus.RECRUITING, 1L),
                        makeStudyWithId(makeBookWithId(1L), StudyStatus.RECRUITING, 3L)
                    ).stream()
                    .map(studyConverter::studyToStudyInfo)
                    .toList()
            );

            given(studyQueryService.getStudies(anyLong(), any(Pageable.class)))
                .willReturn(response);

            ResultActions result = mockMvc.perform(
                get("/api/studies")
                    .param("bookId", String.valueOf(bookId))
                    .param("size", "2")
                    .param("page", "1")
            );

            result.andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            String dataPath = "data.studies[]";
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
                    fieldWithPath(dataPath + ".thumbnail").type(JsonFieldType.STRING)
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
                        .description("스터디 진행 종료 일자")
                )
            );
        }
    }

    @Nested
    @DisplayName("스터디 가입 신청 #52")
    class RequestStudyJoinTest {

        @Test
        void requestStudyJoin() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            Long studyId = 1L;
            Long studyMemberId = 1L;

            given(studyCommandService.requestStudyJoin(studyId, givenUser.userInfo().id()))
                .willReturn(studyMemberId);

            ResultActions result = mockMvc.perform(
                put("/api/studies/{studyId}/members", studyId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()));

            result.andExpect(status().isOk())
                .andDo(documentation())
                .andExpect(jsonPath("$.data").value(studyMemberId));

        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-join-request",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 가입 신청")
                    .description("스터디 가입 신청에 사용되는 API입니다.")
                    .responseSchema(Schema.schema("스터디 가입 요청 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader(),
                pathParameters(
                    parameterWithName("studyId").description("스터디 Id")
                ),
                responseFields(
                    fieldWithPath("data").description("스터디 멤버 Id")
                ));
        }
    }

    @Nested
    @DisplayName("스터디 상세 조회 #56")
    class StudyDetailTest {

        private static Long userId = 1L;

        @Test
        @DisplayName("S 스터디와 관련된 책과 스터디멤버 정보를 같이 조회할 수 있다")
        void studyDetailInfoTest() throws Exception {

            StudyDetailWithMembers expected = StudyDetailWithMembers.builder()
                .study(givenStudyInfo())
                .book(givenStudyBookInfo())
                .members(List.of(givenUserInfo(), givenUserInfo()))
                .build();

            given(studyQueryService.getStudyInfoWithMembers(anyLong())).willReturn(expected);

            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(get("/api/studies/{studyId}", givenUser.userInfo().id())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-detail-info",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 상세 조회 API")
                    .description("스터디와 관련된 책과 스터디멤버 정보를 같이 조회할 수 있는 API 입니다"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("studyId").description("스터디 아이디")
                ),
                responseFields(
                    // study info
                    fieldWithPath("data.study.id").description("스터디 아이디"),
                    fieldWithPath("data.study.name").description("스터디 이름"),
                    // fieldWithPath("data.study.status").description("스터디 진행 상태"), //TODO: ?
                    fieldWithPath("data.study.thumbnail").description("스터디 썸네일"),
                    fieldWithPath("data.study.description").description("스터디 설명"),
                    fieldWithPath("data.study.currentParticipant").description("스터디 현재 참여 인원"),
                    fieldWithPath("data.study.maxParticipant").description("스터디 최대 참여 인원"),
                    fieldWithPath("data.study.gatherStartDate").description("스터디 모집 시작일"),
                    fieldWithPath("data.study.gatherEndDate").description("스터디 모집 종료일"),
                    fieldWithPath("data.study.studyStartDate").description("스터디 시작 일자"),
                    fieldWithPath("data.study.studyEndDate").description("스터디 종료 일자"),

                    // study book info
                    fieldWithPath("data.book.id").description("스터디 책 아이디"),
                    fieldWithPath("data.book.title").description("스터디 책 제목"),
                    fieldWithPath("data.book.image").description("스터디 책 썸네일"),
                    fieldWithPath("data.book.author").description("스터디 책 저자"),
                    fieldWithPath("data.book.publisher").description("스터디 책 출판사"),
                    fieldWithPath("data.book.isbn").description("스터디 책 ISBN"),
                    fieldWithPath("data.book.pubDate").description("스터디 책 출판일"),
                    fieldWithPath("data.book.description").description("스터디 책 설명"),
                    fieldWithPath("data.book.createdAt").description("스터디 책 썸네일"),

                    // study member info
                    fieldWithPath("data.members[].id").
                        description("스터디 멤버 아이디"),
                    fieldWithPath("data.members[].name")
                        .description("스터디 멤버 이름"),
                    fieldWithPath("data.members[].email")
                        .description("스터디 멤버 이메일"),
                    fieldWithPath("data.members[].temperature")
                        .description("스터디 멤버 온도"),
                    fieldWithPath("data.members[].image")
                        .description("스터디 멤버 이미지 URL")
                ));
        }

        private StudyUserInfo givenUserInfo() {
            return StudyUserInfo.builder()
                .id(userId++)
                .name(UUID.randomUUID().toString().substring(10))
                .email("asdf@asdf.com")
                .temperature(36.5f)
                .image("url")
                .build();
        }

        private StudyBookInfo givenStudyBookInfo() {
            return StudyBookInfo.builder()
                .id(1L)
                .title("책 제목")
                .image("책 썸네일")
                .author("책 저자")
                .publisher("출판사")
                .pubDate(LocalDate.now())
                .isbn("1111122222333")
                .description("책 설명설명")
                .createdAt(LocalDateTime.now())
                .build();
        }

        private StudyInfo givenStudyInfo() {
            return StudyInfo.builder()
                .id(1L)
                .name("스터디 이름")
                .thumbnail("스터디 썸네일 URL")
                .description("스터디 설명")
                .currentParticipant(2)
                .maxParticipant(5)
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build();
        }

    }

    @Nested
    @DisplayName("스터디 신청목록 조회")
    class StudyApplierApiTest {

        @Test
        void getAppliersApiSuccess() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            Long studyId = 1L;

            given(studyQueryService.getStudyAppliers(anyLong(), anyLong()))
                .willReturn(
                    new StudyAppliers(userInfoList()));

            mockMvc.perform(get("/api/studies/{studyId}/members", studyId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("study-join-request-list",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("스터디 신청자 목록 조회 API")
                    .description("해당하는 스터디에 대해 아직 처리되지 않은 신청자 목록을 가져옵니다"),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("studyId").description("스터디 아이디")
                ),
                responseFields(
                    fieldWithPath("data.appliers[].id").
                        description("스터디 신청 멤버 아이디"),
                    fieldWithPath("data.appliers[].name")
                        .description("스터디 신청 멤버 이름"),
                    fieldWithPath("data.appliers[].email")
                        .description("스터디 신청 멤버 이메일"),
                    fieldWithPath("data.appliers[].temperature")
                        .description("스터디 신청 멤버 온도"),
                    fieldWithPath("data.appliers[].image")
                        .description("스터디 신청 멤버 이미지 URL")
                ));
        }

        private List<StudyUserInfo> userInfoList() {

            return LongStream.range(1, 3).mapToObj(this::createUserInfo).toList();
        }

        private StudyUserInfo createUserInfo(Long id) {
            return StudyUserInfo.builder()
                .id(id)
                .temperature(36.5f)
                .email("abc" + id + "@naver.com")
                .name("abc" + id)
                .image("https://north/foo.png")
                .build();
        }
    }

    @Nested
    @DisplayName("내 스터디 참여 현황 조회 #116")
    class GetMyStudiesTest {

        @Test
        @DisplayName("S 내가 참여한 스터디 목록을 조회한다.")
        void getMyStudies() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            List<Studies> studies = makeMyStudies();
            MyStudies response = new MyStudies(
                makeUserInfo(), studies.get(0), studies.get(1), studies.get(2)
            );
            given(studyUserFacade.getMyStudies(anyLong()))
                .willReturn(response);

            ResultActions result = mockMvc.perform(get("/api/studies/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
            );

            result.andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            String userPath = "data.user";
            String participationPath = "data.participation.studies[]";
            String finishedPath = "data.finished.studies[]";
            String ownedPath = "data.owned.studies[]";
            return MockMvcRestDocumentationWrapper.document("study-get-me",
                ResourceSnippetParameters.builder()
                    .tag("Study API")
                    .summary("내 스터디 목록 확인")
                    .description("내 스터디 목록을 확인하는 API 입니다.")
                    .responseSchema(Schema.schema("내 스터디 목록 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader(),
                responseFields(
                    fieldWithPath(userPath + ".id").type(JsonFieldType.NUMBER).description("유저 ID"),
                    fieldWithPath(userPath + ".name").type(JsonFieldType.STRING)
                        .description("유저 이름"),
                    fieldWithPath(userPath + ".email").type(JsonFieldType.STRING)
                        .description("유저 이메일"),
                    fieldWithPath(userPath + ".temperature").type(JsonFieldType.NUMBER)
                        .description("유저 온도"),
                    fieldWithPath(userPath + ".image").type(JsonFieldType.STRING)
                        .description("유저 프로필 이미지"),
                    fieldWithPath(participationPath + ".id").type(JsonFieldType.NUMBER)
                        .description("스터디 ID"),
                    fieldWithPath(participationPath + ".name").type(JsonFieldType.STRING)
                        .description("스터디 이름"),
                    fieldWithPath(participationPath + ".thumbnail").type(JsonFieldType.STRING)
                        .description("스터디 썸네일"),
                    fieldWithPath(participationPath + ".description").type(JsonFieldType.STRING)
                        .description("스터디 설명"),
                    fieldWithPath(participationPath + ".currentParticipant").type(
                        JsonFieldType.NUMBER).description("현재 스터디 참가 인원"),
                    fieldWithPath(participationPath + ".maxParticipant").type(JsonFieldType.NUMBER)
                        .description("최대 스터디 참가 인원"),
                    fieldWithPath(participationPath + ".gatherStartDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 시작 일자"),
                    fieldWithPath(participationPath + ".gatherEndDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 종료 일자"),
                    fieldWithPath(participationPath + ".studyStartDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 시작 일자"),
                    fieldWithPath(participationPath + ".studyEndDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 종료 일자"),
                    fieldWithPath(finishedPath + ".id").type(JsonFieldType.NUMBER)
                        .description("스터디 ID"),
                    fieldWithPath(finishedPath + ".name").type(JsonFieldType.STRING)
                        .description("스터디 이름"),
                    fieldWithPath(finishedPath + ".thumbnail").type(JsonFieldType.STRING)
                        .description("스터디 썸네일"),
                    fieldWithPath(finishedPath + ".description").type(JsonFieldType.STRING)
                        .description("스터디 설명"),
                    fieldWithPath(finishedPath + ".currentParticipant").type(
                        JsonFieldType.NUMBER).description("현재 스터디 참가 인원"),
                    fieldWithPath(finishedPath + ".maxParticipant").type(JsonFieldType.NUMBER)
                        .description("최대 스터디 참가 인원"),
                    fieldWithPath(finishedPath + ".gatherStartDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 시작 일자"),
                    fieldWithPath(finishedPath + ".gatherEndDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 종료 일자"),
                    fieldWithPath(finishedPath + ".studyStartDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 시작 일자"),
                    fieldWithPath(finishedPath + ".studyEndDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 종료 일자"),
                    fieldWithPath(ownedPath + ".id").type(JsonFieldType.NUMBER)
                        .description("스터디 ID"),
                    fieldWithPath(ownedPath + ".name").type(JsonFieldType.STRING)
                        .description("스터디 이름"),
                    fieldWithPath(ownedPath + ".thumbnail").type(JsonFieldType.STRING)
                        .description("스터디 썸네일"),
                    fieldWithPath(ownedPath + ".description").type(JsonFieldType.STRING)
                        .description("스터디 설명"),
                    fieldWithPath(ownedPath + ".currentParticipant").type(
                        JsonFieldType.NUMBER).description("현재 스터디 참가 인원"),
                    fieldWithPath(ownedPath + ".maxParticipant").type(JsonFieldType.NUMBER)
                        .description("최대 스터디 참가 인원"),
                    fieldWithPath(ownedPath + ".gatherStartDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 시작 일자"),
                    fieldWithPath(ownedPath + ".gatherEndDate").type(JsonFieldType.STRING)
                        .description("스터디 모집 종료 일자"),
                    fieldWithPath(ownedPath + ".studyStartDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 시작 일자"),
                    fieldWithPath(ownedPath + ".studyEndDate").type(JsonFieldType.STRING)
                        .description("스터디 진행 종료 일자")

                )
            );
        }
    }
}
