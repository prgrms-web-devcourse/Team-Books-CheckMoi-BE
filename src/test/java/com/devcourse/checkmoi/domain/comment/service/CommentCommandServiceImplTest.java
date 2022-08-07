package com.devcourse.checkmoi.domain.comment.service;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeComment;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePost;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Create;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import com.devcourse.checkmoi.domain.post.exception.PostNotFoundException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinRequestNotFoundException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.IntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentCommandServiceImplTest extends IntegrationTest {

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected StudyRepository studyRepository;

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected StudyMemberRepository studyMemberRepository;

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    private CommentCommandServiceImpl commentCommandService;

    private User givenUser;

    private Post givenPost;

    private Post finishPost;

    private Study givenStudy;

    private User otherUser;

    private Study finishStudy;

    @BeforeEach
    void setBasicGiven() {
        Book book = bookRepository.save(makeBook());
        // user
        User user = userRepository.save(makeUser());
        givenUser = userRepository.save(makeUser());
        otherUser = userRepository.save(makeUser());
        // study
        givenStudy = studyRepository.save(makeStudy(book, IN_PROGRESS));
        finishStudy = studyRepository.save(makeStudy(book, FINISHED));

        // studyMember
        studyMemberRepository.save(makeStudyMember(givenStudy, user, StudyMemberStatus.OWNED));
        studyMemberRepository.save(
            makeStudyMember(givenStudy, givenUser, StudyMemberStatus.ACCEPTED));
        studyMemberRepository.save(makeStudyMember(givenStudy, user, StudyMemberStatus.OWNED));

        // post
        givenPost = postRepository.save(makePost(GENERAL, givenStudy, givenUser));
        finishPost = postRepository.save(makePost(GENERAL, finishStudy, givenUser));

    }

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        studyMemberRepository.deleteAllInBatch();
        studyRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("댓글 삭제 #136")
    class DeleteComment {

        Comment comment;

        @BeforeEach
        void createComment() {
            comment = commentRepository.save(makeComment(givenPost, givenUser));
            assertThat(commentRepository.existsById(comment.getId())).isTrue();
        }

        @Test
        @DisplayName("S 댓글 작성자는 댓글을 삭제할 수 있다.")
        void deleteComment() {
            commentCommandService.deleteById(givenUser.getId(), comment.getId());
            assertThat(commentRepository.existsById(comment.getId())).isFalse();
        }
    }

    @Nested
    @DisplayName("댓글 작성 #129")
    class CreateCommentTest {

        Create request = Create.builder()
            .content("댓글 작성 테스트입니다.")
            .build();

        @Test
        @DisplayName("S 게시글에 댓글을 작성할 수 있다.")
        void createComment() {
            Long got = commentCommandService.createComment(
                givenStudy.getId(),
                givenPost.getId(),
                givenUser.getId(),
                request
            );
            Optional<Comment> want = commentRepository.findById(got);

            assertThat(want).isPresent();
            assertThat(want.get().getContent()).isEqualTo(request.content());
        }

        @Test
        @DisplayName("F 존재하지 않는 스터디의 경우 예외발생")
        void studyNotFound() {
            Long notExistStudyId = 0L;
            assertThatExceptionOfType(StudyNotFoundException.class)
                .isThrownBy(() -> commentCommandService.createComment(
                    notExistStudyId,
                    givenPost.getId(),
                    givenUser.getId(),
                    request
                ));
        }

        @Test
        @DisplayName("F 이미 종료된 스터디의 경우 예외발생")
        void finishedStudy() {
            assertThatExceptionOfType(FinishedStudyException.class)
                .isThrownBy(() -> commentCommandService.createComment(
                    finishStudy.getId(),
                    finishPost.getId(),
                    givenUser.getId(),
                    request
                ));
        }

        @Test
        @DisplayName("F 포스트가 존재하지 않을 경우 예외 발생")
        void notExistPost() {
            Long notExistPostId = 0L;
            assertThatExceptionOfType(PostNotFoundException.class)
                .isThrownBy(() -> commentCommandService.createComment(
                    givenStudy.getId(),
                    notExistPostId,
                    givenUser.getId(),
                    request
                ));

        }

        @Test
        @DisplayName("F 유저가 실제로 존재하지 않을 경우 예외 발생")
        void notExistUser() {
            Long notExistUserId = 0L;
            assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> commentCommandService.createComment(
                    givenStudy.getId(),
                    givenPost.getId(),
                    notExistUserId,
                    request
                ));
        }

        @Test
        @DisplayName("F 유저가 실제 스터디에 가입하지 않았을 경우 예외 발생")
        void notJoinStudyUser() {
            assertThatExceptionOfType(StudyJoinRequestNotFoundException.class)
                .isThrownBy(() -> commentCommandService.createComment(
                    givenStudy.getId(),
                    givenPost.getId(),
                    otherUser.getId(),
                    request
                ));
        }
    }
}
