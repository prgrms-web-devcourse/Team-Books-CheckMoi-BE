package com.devcourse.checkmoi.domain.comment.model;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePost;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

    private Post post = makePost();

    private String content = "댓글 내용입니다";

    private User writer = makeUser();

    @Test
    @DisplayName("S 댓글 내용을 수정할 수 있다")
    void editContent() {
        Comment comment = Comment.builder()
            .content(content)
            .post(post)
            .user(writer)
            .build();

        String editedComment = "수정된 댓글";

        comment.editComment(editedComment);

        Assertions.assertThat(comment.getContent())
            .isEqualTo(editedComment);
    }
}