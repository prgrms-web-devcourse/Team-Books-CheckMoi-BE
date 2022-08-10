package com.devcourse.checkmoi.domain.post.service.validator;

import com.devcourse.checkmoi.domain.post.exception.NotAllowedWriterException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.study.exception.ClosedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServiceValidatorTest {

    private final PostServiceValidator validator = new PostServiceValidatorImpl();

    @Test
    @DisplayName("F 해당 스터디의 스터디원이 아닐 경우 예외가 발생한다")
    void validateStudyMember() {
        Long anyLong = 1L;
        StudyMember notStudyMember = StudyMember.builder()
            .status(StudyMemberStatus.PENDING)
            .build();

        Assertions.assertThatThrownBy(() ->
                validator.checkJoinedMember(notStudyMember, anyLong))
            .isInstanceOf(NotJoinedMemberException.class);
    }

    @Test
    @DisplayName("F 스터디장이 아닌 일반 스터디원이 공지를 작성할 경우 예외가 발생한다")
    void validateAllowedPostWriter() {
        Post post = Post.builder()
            .category(PostCategory.NOTICE)
            .build();

        StudyMember member = StudyMember.builder()
            .status(StudyMemberStatus.ACCEPTED)
            .build();

        Assertions.assertThatThrownBy(() ->
                validator.checkAllowedWriter(post, member))
            .isInstanceOf(NotAllowedWriterException.class);
    }


    @Test
    @DisplayName("S 스터디장은 공지 게시글을 작성할 수 있다")
    void validateAllowedPostWriterFail() {
        Post post = Post.builder()
            .category(PostCategory.NOTICE)
            .build();

        StudyMember member = StudyMember.builder()
            .status(StudyMemberStatus.OWNED)
            .build();

        Assertions.assertThatNoException()
            .isThrownBy(() ->
                validator.checkAllowedWriter(post, member));
    }

    @Test
    @DisplayName("S 종료된 스터디는 자유게시글을 작성할 수 있다")
    void validateWritingAllowedPost() {
        Long anyLong = 1L;

        Study study = Study.builder()
            .status(StudyStatus.FINISHED)
            .id(anyLong)
            .build();

        Post post = Post.builder()
            .category(PostCategory.GENERAL)
            .study(study)
            .build();

        Assertions.assertThatNoException()
            .isThrownBy(() ->
                validator.checkWritingAllowedPost(post, study.getId()));
    }


    @Test
    @DisplayName("F 종료된 스터디는 공지를 작성할 수 없다")
    void validateWritingAllowedPostFail() {
        Long anyLong = 1L;

        Study study = Study.builder()
            .status(StudyStatus.FINISHED)
            .id(anyLong)
            .build();

        Post post = Post.builder()
            .category(PostCategory.NOTICE)
            .study(study)
            .build();

        Assertions.assertThatThrownBy(() ->
                validator.checkWritingAllowedPost(post, study.getId()))
            .isInstanceOf(ClosedStudyException.class);
    }
}