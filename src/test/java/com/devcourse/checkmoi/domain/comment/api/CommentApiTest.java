package com.devcourse.checkmoi.domain.comment.api;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.service.CommentQueryService;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

class CommentApiTest extends IntegrationTest {

    @MockBean
    private CommentQueryService commentQueryService;

    @Nested
    @DisplayName("작성된 글에 대한 댓글 목록 조회 #130")
    class FindAllComments {

        @Test
        @DisplayName("S 해당 포스트에 작성한 글을 조회할 수 있다")
        void findAllComments() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            User writer = makeUserWithId(1L);

            List<CommentInfo> response = List.of(
                makeCommentInfoWithId(writer, 1L),
                makeCommentInfoWithId(writer, 2L),
                makeCommentInfoWithId(writer, 3L)
            );

            Search request = Search.builder()
                .postId(1L)
                .build();

            when(commentQueryService.findAllComments(anyLong(), any(Search.class)))
                .thenReturn(response);

            mockMvc.perform(get("/api/comments")
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .content(toJson(request)))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("find-comments",
                ResourceSnippetParameters.builder()
                    .tag("Comment API")
                    .summary("댓글 검색")
                    .description("댓글 검색에 사용되는 API")
                    .requestSchema(Schema.schema("댓글 검색 요청"))
                    .responseSchema(Schema.schema("댓글 검색 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader(),

                requestFields(
                    fieldWithPath("postId").type(JsonFieldType.NUMBER)
                        .description("게시글 아이디")
                ),
                responseFields(
                    fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                        .description("댓글 아이디"),
                    fieldWithPath("data[].userId").type(JsonFieldType.NUMBER)
                        .description("댓글 작성자 아이디"),
                    fieldWithPath("data[].content").type(JsonFieldType.STRING)
                        .description("댓글 본문"),
                    fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
                        .description("댓글 작성일자"),
                    fieldWithPath("data[].updatedAt").type(JsonFieldType.STRING)
                        .description("댓글 수정일자")
                )
            );
        }

        private CommentInfo makeCommentInfoWithId(User user, Long commentId) {
            return CommentInfo.builder()
                .id(commentId)
                .userId(user.getId())
                .content("댓글 - " + UUID.randomUUID())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        }
    }

}
