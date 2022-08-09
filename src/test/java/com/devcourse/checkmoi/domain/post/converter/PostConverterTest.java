package com.devcourse.checkmoi.domain.post.converter;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePostWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.devcourse.checkmoi.domain.post.dto.PostRequest;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostConverterTest {

    private PostConverter postConverter = new PostConverter();

    @Test
    @DisplayName("Post -> PostInfo 변환 테스트")
    void postToInfo() {
        // given
        User user = makeUser();
        Study study = makeStudy(makeBook(), IN_PROGRESS);
        Post post = makePostWithId(GENERAL, study, user, 1L);

        // when
        PostInfo postInfo = postConverter.postToInfo(post);

        // then
        assertAll(
            () -> assertThat(postInfo.id()).isEqualTo(post.getId()),
            () -> assertThat(postInfo.title()).isEqualTo(post.getTitle()),
            () -> assertThat(postInfo.content()).isEqualTo(post.getContent()),
            () -> assertThat(postInfo.category()).isEqualTo(post.getCategory()),
            () -> assertThat(postInfo.studyId()).isEqualTo(post.getStudy().getId()),
            () -> assertThat(postInfo.writer()).isEqualTo(post.getWriter().getName()),
            () -> assertThat(postInfo.writerImage())
                .isEqualTo(post.getWriter().getProfileImgUrl()),
            () -> assertThat(postInfo.commentCount()).isEqualTo(post.getCommentCount()),
            () -> assertThat(postInfo).hasFieldOrProperty("createdAt"),
            () -> assertThat(postInfo).hasFieldOrProperty("updatedAt")
        );
    }

    @Test
    @DisplayName("Create -> Post 변환 테스트")
    void createToPost() {
        // given
        User writer = makeUserWithId(1L);
        Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 1L);
        PostRequest.Create create = Create.builder()
            .title("타이틀")
            .content("본문")
            .category("GENERAL")
            .studyId(study.getId())
            .build();

        // when
        Post post = postConverter.createToPost(create, writer.getId());

        // then
        assertAll(
            () -> assertThat(post.getTitle()).isEqualTo(create.title()),
            () -> assertThat(post.getContent()).isEqualTo(create.content()),
            () -> assertThat(post.getCategory()).isEqualTo(PostCategory.valueOf(create.category())),
            () -> assertThat(post.getStudy().getId()).isEqualTo(create.studyId()),
            () -> assertThat(post).hasFieldOrProperty("writer")
        );
    }

}
