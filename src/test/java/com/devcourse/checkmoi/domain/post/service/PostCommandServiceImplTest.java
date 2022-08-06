package com.devcourse.checkmoi.domain.post.service;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePostWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.exception.PostNoPermissionException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.post.service.validator.PostServiceValidator;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceImplTest {

    @InjectMocks
    private PostCommandServiceImpl postCommandService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostConverter postConverter;

    @Mock
    private PostServiceValidator postValidator;

    @Nested
    @DisplayName("게시글을 작성할 수 있다 #86")
    class CreatePostTest {

        @Test
        @DisplayName("S 게시글을 작성할 수 있다")
            //TODO: validation
        void createPost() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);

            PostRequest.Create request = Create.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory().toString())
                .studyId(study.getId())
                .build();

            when(postConverter.createToPost(any(Create.class), anyLong())).thenReturn(post);
            when(postRepository.save(any(Post.class))).thenReturn(post);

            Long result = postCommandService.createPost(user.getId(), request);

            assertThat(result).isEqualTo(post.getId());
            then(postRepository).should(times(1)).save(post);
            then(postConverter).should(times(1)).createToPost(request, user.getId());
        }

    }

    @Nested
    @DisplayName("게시글을 수정할 수 있다 #86")
    class EditPostTest {

        @Test
        @DisplayName("S 작성자와 수정하려고 하는 주체가 같다면 게시글을 수정할 수 있다")
        void editPost() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);

            PostRequest.Edit request = Edit.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .studyId(study.getId())
                .build();

            doNothing().when(postValidator).validatePostOwner(anyLong(), anyLong());
            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

            postCommandService.editPost(user.getId(), post.getId(), request);

            then(postRepository).should(times(1)).findById(post.getId());
        }

        @Test
        @DisplayName("F 작성자와 수정하려고 하는 주체가 다르다면 게시글을 수정할 수 없다")
        void editPostFailed() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);

            PostRequest.Edit request = Edit.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .studyId(study.getId())
                .build();

            doThrow(PostNoPermissionException.class)
                .when(postValidator)
                .validatePostOwner(anyLong(), anyLong());

            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

            assertThatExceptionOfType(PostNoPermissionException.class)
                .isThrownBy(() -> postCommandService.editPost(user.getId(), post.getId(), request));

            then(postRepository).should(times(1)).findById(post.getId());
        }

    }

    @Nested
    @DisplayName("게시글을 삭제할 수 있다 #86")
    class DeletePostTest {

        @Test
        @DisplayName("S 작성자와 지우고자 하는 주체가 같다면 게시글을 지울 수 있다")
        void deletePost() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);

            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
            doNothing().when(postValidator).validatePostOwner(anyLong(), anyLong());

            postCommandService.deletePost(user.getId(), post.getId());

            then(postRepository).should(times(1)).findById(post.getId());
        }

        @Test
        @DisplayName("F 작성자와 지우고자 하는 주체가 다르다면 게시글을 지울 수 없다")
        void deletePostFailed() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);

            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
            doThrow(PostNoPermissionException.class)
                .when(postValidator)
                .validatePostOwner(anyLong(), anyLong());

            assertThatExceptionOfType(PostNoPermissionException.class)
                .isThrownBy(() -> postCommandService.deletePost(user.getId(), post.getId()));

            then(postRepository).should(times(1)).findById(post.getId());
        }

    }
}
