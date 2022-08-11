package com.devcourse.checkmoi.domain.comment.facade;

import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeCommentInfo;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makePostInfo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.checkmoi.domain.comment.service.CommentCommandService;
import com.devcourse.checkmoi.domain.comment.service.CommentQueryService;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.global.model.SimplePage;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CommentFacadeTest {

    @InjectMocks
    CommentFacade commentFacade;

    @Mock
    PostQueryService postQueryService;

    @Mock
    StudyQueryService studyQueryService;

    @Mock
    CommentCommandService commentCommandService;

    @Mock
    CommentQueryService commentQueryService;

    @Nested
    @DisplayName("댓글 생성 #129")
    class CreateCommentTest {

        @Test
        @DisplayName("S 댓글을 생성할 수 있다")
        void createComment() {
            Create request = new Create("댓글 내용");
            PostInfo postInfo = makePostInfo();
            Long commentId = 1L;
            given(postQueryService.findByPostId(anyLong(), anyLong()))
                .willReturn(postInfo);
            given(commentCommandService.createComment(anyLong(), anyLong(), any(Create.class)))
                .willReturn(commentId);

            Long got = commentFacade.createComment(postInfo.id(), postInfo.studyId(), request);

            assertThat(got).isEqualTo(commentId);
        }
    }

    @Nested
    @DisplayName("댓글 조회 페이지 적용 #178")
    class FindAllCommentsTest {

        @Test
        @DisplayName("페이지 적용 댓글 조회")
        void findAllComments() {
            Long userId = 1L;
            Long postId = 1L;
            Search search = new Search(postId);
            PostInfo postInfo = makePostInfo();
            SimplePage simplePage = SimplePage.builder()
                .page(1)
                .size(3)
                .build();
            long totalPage = 1L;
            Comments want = new Comments(
                List.of(
                    makeCommentInfo(1L, 1L, 1L),
                    makeCommentInfo(2L, 1L, 1L),
                    makeCommentInfo(3L, 1L, 1L)
                ), totalPage
            );

            given(postQueryService.findByPostId(anyLong(), anyLong()))
                .willReturn(postInfo);
            given(commentQueryService.findAllComments(any(Search.class), any(Pageable.class)))
                .willReturn(want);

            Comments got = commentFacade.findAllComments(userId, search, simplePage.pageRequest());
            Assertions.assertThat(got.comments()).hasSize(want.comments().size());
            Assertions.assertThat(want.totalPage()).isEqualTo(want.totalPage());
        }
    }
}