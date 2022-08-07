package com.devcourse.checkmoi.domain.comment.converter;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeCommentWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePostWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentConverterTest {

    private CommentConverter commentConverter = new CommentConverter();

    @Test
    @DisplayName("Comment -> CommentInfo 변환 테스트")
    void commentToInfo() {
        User user = makeUser();
        Study study = makeStudy(makeBook(), IN_PROGRESS);
        Post post = makePostWithId(GENERAL, study, user, 1L);
        Comment comment = makeCommentWithId(post, user, 1L);

        CommentInfo commentInfo = commentConverter.commentToInfo(comment);

        assertAll(
            () -> assertThat(commentInfo.id()).isEqualTo(comment.getId()),
            () -> assertThat(commentInfo.userId()).isEqualTo(comment.getUser().getId()),
            () -> assertThat(commentInfo.content()).isEqualTo(comment.getContent()),
            () -> assertThat(commentInfo).hasFieldOrProperty("createdAt"),
            () -> assertThat(commentInfo).hasFieldOrProperty("updatedAt")
        );
    }
}
