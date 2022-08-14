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
import com.devcourse.checkmoi.domain.post.dto.PostResponse.Posts;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.service.PostCommandService;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
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

    @Autowired
    PostConverter postConverter;

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
                    .summary("게시글 생성 API")
                    .description("게시글을 생성하는 API 입니다.")
                    .requestSchema(Schema.schema("게시글 생성 요청"))
                    .responseSchema(Schema.schema("게시글 생성 응답")),
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
                .writerImage(givenUser.userInfo().image())
                .commentCount(12)

                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

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
                    .summary("게시글 단일 조회 API")
                    .description("게시글을 단일 조회하는 API 입니다.")
                    .requestSchema(Schema.schema("게시글 단일 조회 요청"))
                    .responseSchema(Schema.schema("게시글 단일 조회 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }
    }

    @Nested
    @DisplayName("게시글을 다중 조회할 수 있다 #86")
    class FindAllPostsTest {

        @Test
        void findAllPosts() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            Study study = makeStudyWithId(makeBook(), StudyStatus.IN_PROGRESS, 1L);
            User user = makeUserWithId(givenUser.userInfo().id());

            int page = 0;
            int size = 3;
            String category = "GENERAL";
            String direction = "ASC";

            List<PostInfo> postInfos = List.of(
                makePostInfos(makePostWithId(GENERAL, study, user, 1L)),
                makePostInfos(makePostWithId(GENERAL, study, user, 2L)),
                makePostInfos(makePostWithId(GENERAL, study, user, 3L))
            );

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("studyId", String.valueOf(study.getId()));
            params.add("page", String.valueOf(page));
            params.add("size", String.valueOf(size));
            params.add("category", category);
            params.add("direction", direction);

            given(postQueryService.findAllByCondition(anyLong(), any(Search.class),
                any(SimplePage.class)))
                .willReturn(new Posts(1L, postInfos));

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
                .writerId(post.getWriter().getId())
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
                    .summary("게시글 검색 API ")
                    .description("게시글을 검색하는 API 입니다.")
                    .requestSchema(Schema.schema("게시글 검색 요청"))
                    .responseSchema(Schema.schema("게시글 검색 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader(),
                requestParameters(
                    parameterWithName("studyId").description("스터디 아이디").optional(),
                    parameterWithName("page").description("목록 페이지").optional(),
                    parameterWithName("size").description("목록 크기").optional(),
                    parameterWithName("category").description("게시글 카테고리리(NOTICE,GENERAL)")
                        .optional(),
                    parameterWithName("direction").description("정렬조건(최신순,오래된순: DESC,ASC)")
                        .optional()
                ),
                responseFields(
                    fieldWithPath("data.totalPage").description("게시글 페이지 총 개수"),
                    fieldWithPath("data.posts[].id").description("게시글 아이디"),
                    fieldWithPath("data.posts[].title").description("게시글 제목"),
                    fieldWithPath("data.posts[].content").description("게시글 본문"),
                    fieldWithPath("data.posts[].category").description("게시글 카테고리"),
                    fieldWithPath("data.posts[].studyId").description("게시글이 작성된 스터디"),
                    fieldWithPath("data.posts[].writer").description("게시글을 작성한 유저 이름"),
                    fieldWithPath("data.posts[].writerId").description("게시글을 작성한 유저 식별자"),
                    fieldWithPath("data.posts[].writerImage").description("게시글을 작성한 유저 프로필 사진"),
                    fieldWithPath("data.posts[].commentCount").description("게시글 댓글 수"),
                    fieldWithPath("data.posts[].createdAt").description("게시글 작성 일자"),
                    fieldWithPath("data.posts[].updatedAt").description("게시글 수정 일자")
                )
            );
        }
    }

    @Nested
    @DisplayName("게시글을 수정할 수 있다 #86")
    class EditPostTest {

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
                    .summary("게시글 수정 API")
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
                    .summary("게시글 삭제 API")
                    .description("게시글을 삭제하는 API 입니다.")
                    .requestSchema(Schema.schema("게시글 삭제 요청"))
                    .responseSchema(Schema.schema("게시글 삭제 응답")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }
    }

}
