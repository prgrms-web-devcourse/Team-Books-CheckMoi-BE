package com.devcourse.checkmoi.domain.post.service;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePostWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.post.service.validator.PostServiceValidator;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostQueryServiceImplTest {

    @InjectMocks
    private PostQueryServiceImpl postQueryService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostConverter postConverter;


    @Nested
    @DisplayName("게시글을 다중 조회할 수 있다 #86")
    class FindAllPostsTest {

        private PostConverter postConverter = new PostConverter();

        @Test
        @DisplayName("S 게시글을 다중 조회할 수 있다")
        void findAllPosts() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);

            List<Post> posts = List.of(
                makePostWithId(GENERAL, study, user, 3L),
                makePostWithId(GENERAL, study, user, 4L),
                makePostWithId(GENERAL, study, user, 5L)
            );

            Search request = Search.builder().id(3L).build(); // TODO: 얘로 검색

            when(postRepository.findAll()).thenReturn(posts);
            postQueryService.findAllPosts(user.getId(), Search.builder().build());
            then(postRepository).should(times(1)).findAll();
        }

    }

    @Nested
    @DisplayName("게시글을 단일 조회할 수 있다 #86")
    class FindPostTest {

        @Mock
        private PostServiceValidator postValidator;

        @Test
        @DisplayName("S 게시글을 단건 조회할 수 있다")
        void findPost() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);

            PostInfo postInfo = PostInfo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory().toString())
                .studyId(post.getStudy().getId())
                .writerId(post.getWriter().getId())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

            when(postConverter.postToInfo(any(Post.class))).thenReturn(postInfo);
            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

            postQueryService.findByPostId(user.getId(), post.getId());

            then(postRepository).should(times(1)).findById(post.getId());
            then(postConverter).should(times(1)).postToInfo(post);
        }

        @Test
        @DisplayName("F 해당 하는 게시글이 없다면 조회할 수 없다.")
        void findPostFailed() {
            User user = makeUserWithId(1L);
            Study study = makeStudyWithId(makeBook(), IN_PROGRESS, 2L);
            Post post = makePostWithId(GENERAL, study, user, 3L);
            Long illegalPostId = 5L;

            when(postRepository.findById(anyLong())).thenThrow(new PostNotFoundException());

            assertThatExceptionOfType(PostNotFoundException.class)
                .isThrownBy(() -> postQueryService.findByPostId(user.getId(), illegalPostId));
        }
    }

}
