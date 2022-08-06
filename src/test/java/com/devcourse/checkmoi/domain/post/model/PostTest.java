package com.devcourse.checkmoi.domain.post.model;

import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.util.EntityGeneratorUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    private User writer = EntityGeneratorUtil.makeUser();

    private String content = "비어있지 않은 컨텐츠";

    private String title = "비어있지 않은 제목";

    @Test
    @DisplayName("S 게시글의 컨텐츠를 수정할 수 있다")
    void editPostContent() {
        Post post = Post.builder()
            .writer(writer)
            .content(content)
            .category(PostCategory.GENERAL)
            .title(title)
            .build();
        String editedContent = "수정된 내용";

        post.editContent(editedContent);

        Assertions.assertThat(post.getContent())
            .isEqualTo(editedContent);
    }

    @Test
    @DisplayName("S 게시글의 제목을 수정할 수 있다")
    void editPostTitle() {
        Post post = Post.builder()
            .writer(writer)
            .content(content)
            .category(PostCategory.GENERAL)
            .title(title)
            .build();
        String editedTitle = "수정된 제목";

        post.editTitle(editedTitle);

        Assertions.assertThat(post.getTitle())
            .isEqualTo(editedTitle);
    }

    @Test
    @DisplayName("F 존재하지 않은 카테고리 정보를 통해 게시글 작성시 게시글 작성에 실패한다")
    void createPostFail() {
        User writer = EntityGeneratorUtil.makeUser();

        Assertions.assertThatThrownBy(() ->
            Post.builder()
                .writer(writer)
                .content("컨텐츠")
                .category(PostCategory.valueOf("익명게시판"))
                .title("제목")
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }


}
