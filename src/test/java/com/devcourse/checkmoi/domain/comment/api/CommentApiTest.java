package com.devcourse.checkmoi.domain.comment.api;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeCommentInfoWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeCommentWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePostWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Edit;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.checkmoi.domain.comment.exception.CommentNotFoundException;
import com.devcourse.checkmoi.domain.comment.facade.CommentFacade;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.comment.service.CommentCommandService;
import com.devcourse.checkmoi.domain.comment.service.CommentQueryService;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class CommentApiTest extends IntegrationTest {

    @MockBean
    private CommentQueryService commentQueryService;

    @MockBean
    private CommentCommandService commentCommandService;

    @MockBean
    private CommentFacade commentFacade;

    @Nested
    @DisplayName("댓글 목록 조회 #130")
    class FindAllCommentsTest {

        @Test
        @DisplayName("S 검색조건(ex- 포스트ID)에 따라 작성된 댓글을 조회할 수 있다")
        void findAllComments() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            User writer = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, writer, 1L);
            Comments response = Comments.builder().comments(
                List.of(makeCommentInfoWithId(writer, post, 1L),
                    makeCommentInfoWithId(writer, post, 2L),
                    makeCommentInfoWithId(writer, post, 3L))).totalPage(1L).build();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("postId", String.valueOf(1L));

            when(commentFacade.findAllComments(anyLong(), any(Search.class),
                any(Pageable.class))).thenReturn(response);

            mockMvc.perform(get("/api/comments").header(HttpHeaders.AUTHORIZATION,
                    "Bearer " + givenUser.accessToken()).params(params)).andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            String commentsPath = "data.comments[]";
            return MockMvcRestDocumentationWrapper.document("find-comments",
                ResourceSnippetParameters.builder().tag("Comment API").summary("댓글 검색")
                    .description("댓글 검색에 사용되는 API").requestSchema(Schema.schema("댓글 검색 요청"))
                    .responseSchema(Schema.schema("댓글 검색 응답")), preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()), tokenRequestHeader(),
                requestParameters(parameterWithName("postId").description("게시글 아이디").optional()),
                responseFields(fieldWithPath(commentsPath + ".id").type(JsonFieldType.NUMBER)
                        .description("댓글 아이디"),
                    fieldWithPath(commentsPath + ".userId").type(JsonFieldType.NUMBER)
                        .description("댓글 작성자 아이디"),
                    fieldWithPath(commentsPath + ".userName").type(JsonFieldType.STRING)
                        .description("댓글 작성자 이름"),
                    fieldWithPath(commentsPath + ".userImage").type(JsonFieldType.STRING)
                        .description("댓글 작성자 이미지"),
                    fieldWithPath(commentsPath + ".postId").type(JsonFieldType.NUMBER)
                        .description("게시글 아이디"),
                    fieldWithPath(commentsPath + ".content").type(JsonFieldType.STRING)
                        .description("댓글 본문"),
                    fieldWithPath(commentsPath + ".createdAt").type(JsonFieldType.STRING)
                        .description("댓글 작성일자"),
                    fieldWithPath(commentsPath + ".updatedAt").type(JsonFieldType.STRING)
                        .description("댓글 수정일자"),
                    fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                        .description("페이지 총 개수")));
        }


    }

    @Nested
    @DisplayName("댓글 삭제 #136")
    class DeleteCommentTest {

        @Test
        @DisplayName("S 댓글 작성자는 댓글을 삭제할 수 있다.")
        void deleteComment() throws Exception {

            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            User writer = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, writer, 1L);
            Comment comment = makeCommentWithId(post, writer, 1L);
            doNothing().when(commentCommandService).deleteById(anyLong(), anyLong());

            mockMvc.perform(delete("/api/comments/{commentId}", comment.getId()).header(
                    HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isNoContent()).andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("delete-comments",
                ResourceSnippetParameters.builder().tag("Comment API").summary("댓글 삭제")
                    .description("댓글 삭제에 사용되는 API"), preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()), tokenRequestHeader(),
                pathParameters(parameterWithName("commentId").description("댓글 Id")));
        }
    }

    @Nested
    @DisplayName("댓글 작성 #129")
    class CreateCommentTest {

        @Test
        @DisplayName("작성된 글에 댓글을 작성할 수 있다.")
        void createCommentTest() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            Create request = Create.builder().content("댓글 내용").build();
            Long studyId = 1L;
            Long postId = 1L;
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, studyId);
            Post post = makePostWithId(GENERAL, study, makeUserWithId(givenUser.userInfo().id()),
                postId);
            Long response = 1L;
            given(commentFacade.createComment(post.getId(), givenUser.userInfo().id(),
                request)).willReturn(response);

            ResultActions result = mockMvc.perform(
                post("/api/comments").header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + givenUser.accessToken()).param("postId", String.valueOf(postId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            result.andExpect(status().isCreated()).andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("create-comments",
                ResourceSnippetParameters.builder().tag("Comment API").summary("댓글 생성")
                    .description("댓글 생성에 사용되는 API").requestSchema(Schema.schema("댓글 생성 요청"))
                    .responseSchema(Schema.schema("댓글 생성 응답")), preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()), tokenRequestHeader(),
                requestParameters(parameterWithName("postId").description("게시글 Id")), requestFields(
                    fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("댓글 ID")));
        }

    }

    @Nested
    @DisplayName("댓글 수정 #137")
    class EditCommentTest {

        TokenWithUserInfo givenUser;

        private Comment comment;

        @BeforeEach
        private void setGiven() {
            givenUser = getTokenWithUserInfo();
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 1L);
            User user = makeUserWithId(givenUser.userInfo().id());
            Post post = makePostWithId(GENERAL, study, user, 1L);

            comment = makeCommentWithId(post, user, 1L);
        }

        @Test
        @DisplayName("S 댓글을 수정할 수 있다")
        void editComment() throws Exception {

            Edit request = Edit.builder().content("댓글 수정 내용").build();

            willDoNothing().given(commentCommandService)
                .editComment(anyLong(), anyLong(), any(Edit.class));

            mockMvc.perform(
                    put("/api/comments/{commentId}", comment.getId()).header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + givenUser.accessToken()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent()).andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("edit-comments",
                ResourceSnippetParameters.builder().tag("Comment API").summary("댓글 수정")
                    .description("댓글 수정에 사용되는 API").requestSchema(Schema.schema("댓글 수정 요청"))
                    .responseSchema(Schema.schema("댓글 수정 응답")), preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()), tokenRequestHeader(),
                pathParameters(parameterWithName("commentId").description("댓글 Id")), requestFields(
                    fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")));
        }

        @Test
        @DisplayName("F 댓글 수정 내용이 500자를 넘어가면 수정할 수 없다")
        void editCommentFailed1() throws Exception {

            String editContent = "1234567890".repeat(51);
            Edit request = Edit.builder().content(editContent).build();

            willDoNothing().given(commentCommandService)
                .editComment(anyLong(), anyLong(), any(Edit.class));

            mockMvc.perform(
                    put("/api/comments/{commentId}", comment.getId()).header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + givenUser.accessToken()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()).andDo(print());
        }

        @Test
        @DisplayName("F 댓글이 없으면 댓글을 수정할 수 없다")
        void editCommentFailed2() throws Exception {

            Edit request = Edit.builder().content("댓글 수정 내용").build();

            willThrow(new CommentNotFoundException()).given(commentCommandService)
                .editComment(anyLong(), anyLong(), any(Edit.class));

            mockMvc.perform(
                    put("/api/comments/{commentId}", comment.getId()).header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + givenUser.accessToken()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound()).andDo(print());
        }

    }

}
