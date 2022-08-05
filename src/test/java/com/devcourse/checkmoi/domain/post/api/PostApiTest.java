package com.devcourse.checkmoi.domain.post.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.service.PostCommandService;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

class PostApiTest extends IntegrationTest {

    @MockBean
    private PostCommandService postCommandService;

    @MockBean
    private PostQueryService postQueryService;

    @Nested
    @DisplayName("게시글을 단일 조회할 수 있다 #86")
    class FindPostTest {

        @Test
        void findPost() throws Exception {
            Long postId = 1L;
            PostInfo postInfo = PostInfo.builder().id(postId).build();
            // TODO: IN_PROGRESS 중인 스터디에서만 포스트가 작성 가능하다
            given(postQueryService.findByPostId(anyLong(), anyLong())).willReturn(postInfo);

            TokenWithUserInfo givenUser = getTokenWithUserInfo();
            mockMvc.perform(get("/api/posts/{postId}", postId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken()))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            //{"data":{"id":1}}
            return MockMvcRestDocumentationWrapper.document("post-detail",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 단일 조회 API")
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

            Search request = Search.builder().id(1L).build();

            List<PostInfo> postInfos = Stream.of(
                Post.builder().id(1L).build(),
                Post.builder().id(2L).build(),
                Post.builder().id(3L).build()
            ).map(postConverter::postToInfo).toList();

            given(postQueryService.findAllPosts(anyLong(), any(Search.class)))
                .willReturn(postInfos);

            mockMvc.perform(get("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .content(toJson(request)))
                .andExpect(status().isOk())
                .andDo(documentation());
        }

        private RestDocumentationResultHandler documentation() {
            //             Body = {"data":[{"id":1},{"id":2},{"id":3}]}
            return MockMvcRestDocumentationWrapper.document("many-post-detail",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 검색 API")
                    .description("게시글을 검색하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
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
            Edit request = Edit.builder().title("수정된 제목").content("수정된 본문").build();

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
            return MockMvcRestDocumentationWrapper.document("post-edit",
                ResourceSnippetParameters.builder()
                    .tag("Post API")
                    .summary("게시글 삭제 API")
                    .description("게시글을 삭제하는 API 입니다."),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                tokenRequestHeader()
            );
        }
    }

}
