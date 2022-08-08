package com.devcourse.checkmoi.domain.post.api;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.post.model.PostCategory.NOTICE;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePostWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
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
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.service.PostCommandService;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class PostApiTest extends IntegrationTest {

    @MockBean
    private PostCommandService postCommandService;

    @MockBean
    private PostQueryService postQueryService;

    @Nested
    @DisplayName("게시글을 작성할 수 있다 #86")
    class CreatePostTest {

        @Test
        void createPost() throws Exception {
            Long createdPostId = 1L;
            PostRequest.Create create = Create.builder()
                .title("게시글 제목 샘플")
                .content("게시글 본문 샘플")
                .category("NOTICE")
                .studyId(1L)
                .build();

            given(postCommandService.createPost(anyLong(), any(Create.class)))
                .willReturn(createdPostId);

            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(post("/api/posts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .content(toJson(create)))
                .andExpect(status().isCreated())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("post-create",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 생성 API (준비중)")
                    .description("게시글을 생성하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader(),
                responseHeaders(
                    headerWithName("Location").description("생성된 게시글 URI")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                    fieldWithPath("category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                    fieldWithPath("studyId").type(JsonFieldType.NUMBER).description("스터디 아이디")
                )
            );
        }
    }

    @Nested
    @DisplayName("게시글을 단일 조회할 수 있다 #86")
    class FindPostTest {

        @Test
        void findPost() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            Long postId = 1L;

            PostInfo postInfo = PostInfo.builder()
                .id(postId)
                .title("샘플 제목")
                .content("샘플 본문")
                .category(NOTICE)
                .studyId(1L)

                .writer(givenUser.userInfo().name())
                .writerImage(givenUser.userInfo().profileImageUrl())
                .commentCount(12)

                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            // TODO: IN_PROGRESS 중인 스터디에서만 포스트가 작성 가능하다
            given(postQueryService.findByPostId(anyLong(), anyLong())).willReturn(postInfo);

            mockMvc.perform(get("/api/posts/{postId}", postId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("post-detail",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 단일 조회 API (준비중)")
                    .description("게시글을 단일 조회하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }
    }

    @Nested
    @DisplayName("게시글을 다중 조회할 수 있다 #86")
    class FindAllPostsTest {

        @Autowired
        PostConverter postConverter;

        @Test
        void findAllPosts() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            Study study = makeStudyWithId(makeBook(), StudyStatus.IN_PROGRESS, 1L);
            User user = makeUserWithId(givenUser.userInfo().id());

            List<PostInfo> postInfos = List.of(
                makePostInfos(makePostWithId(GENERAL, study, user, 1L)),
                makePostInfos(makePostWithId(GENERAL, study, user, 2L)),
                makePostInfos(makePostWithId(GENERAL, study, user, 3L))
            );

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("studyId", String.valueOf(study.getId()));

            given(postQueryService.findAllByCondition(anyLong(), any(Search.class)))
                .willReturn(postInfos);

            mockMvc.perform(get("/api/posts")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .params(params))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private PostInfo makePostInfos(Post post) {
            return PostInfo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .studyId(post.getStudy().getId())
                .writer(post.getWriter().getName())
                .writerImage(post.getWriter().getProfileImgUrl())
                .commentCount(post.getCommentCount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("many-post-detail",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 검색 API (준비중)")
                    .description("게시글을 검색하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader(),
                requestParameters(
                    parameterWithName("studyId").description("스터디 아이디").optional()
                ),
                responseFields(
                    // post infos
                    fieldWithPath("data[].id").description("게시글 아이디"),
                    fieldWithPath("data[].title").description("게시글 제목"),
                    fieldWithPath("data[].content").description("게시글 본문"),
                    fieldWithPath("data[].category").description("게시글 카테고리"),
                    fieldWithPath("data[].studyId").description("게시글이 작성된 스터디"),
                    fieldWithPath("data[].writer").description("게시글을 작성한 유저 이름"),
                    fieldWithPath("data[].writerImage").description("게시글을 작성한 유저 프로필 사진"),
                    fieldWithPath("data[].commentCount").description("게시글 댓글 수"),
                    fieldWithPath("data[].createdAt").description("게시글 작성 일자"),
                    fieldWithPath("data[].updatedAt").description("게시글 수정 일자")
                )
            );
        }
    }

    @Nested
    @DisplayName("게시글을 수정할 수 있다 #86")
    class EditPostTest {

        @Autowired
        PostConverter postConverter;

        @Test
        void editPost() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            Edit request = Edit.builder()
                .title("수정된 제목")
                .content("수정된 본문")
                .studyId(1L)
                .build();

            Long postId = 1L;

            mockMvc.perform(put("/api/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .content(toJson(request)))
                .andExpect(status().isNoContent())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("post-edit",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 수정 API (준비중)")
                    .description("게시글을 수정하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }

    }

    @Nested
    @DisplayName("게시글을 삭제할 수 있다 #86")
    class DeletePostTest {

        @Test
        void deletePost() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            Long postId = 1L;

            mockMvc.perform(delete("/api/posts/{postId}", postId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isNoContent())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("post-delete",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 삭제 API (준비중)")
                    .description("게시글을 삭제하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }
    }

}
