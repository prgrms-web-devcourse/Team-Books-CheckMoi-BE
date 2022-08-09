package com.devcourse.checkmoi.domain.comment.facade;

import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makePostInfo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.service.CommentCommandService;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentCommandFacadeImplTest {

    @InjectMocks
    CommentCommandFacadeImpl commentCommandFacade;

    @Mock
    PostQueryService postQueryService;

    @Mock
    StudyQueryService studyQueryService;

    @Mock
    CommentCommandService commentCommandService;

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

            Long got = commentCommandFacade.createComment(postInfo.id(), postInfo.studyId(), request);

            assertThat(got).isEqualTo(commentId);
        }

    }
}