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
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Edit;
import com.devcourse.checkmoi.domain.comment.exception.CommentNoPermissionException;
import com.devcourse.checkmoi.domain.comment.exception.CommentNotFoundException;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.study.exception.FinishedStudyException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
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

    private User ownedUser;

    private User otherUser;

    private Post givenPost;

    private Comment finishComment;

    @BeforeEach
    void setBasicGiven() {
        Book book = bookRepository.save(makeBook());
        // user
        ownedUser = userRepository.save(makeUser());
        givenUser = userRepository.save(makeUser());
        otherUser = userRepository.save(makeUser());
        // study
        Study givenStudy = studyRepository.save(makeStudy(book, IN_PROGRESS));
        Study finishStudy = studyRepository.save(makeStudy(book, FINISHED));

        // studyMember
        studyMemberRepository.save(makeStudyMember(givenStudy, ownedUser, StudyMemberStatus.OWNED));
        studyMemberRepository.save(
            makeStudyMember(givenStudy, givenUser, StudyMemberStatus.ACCEPTED));
        studyMemberRepository.save(
            makeStudyMember(finishStudy, givenUser, StudyMemberStatus.OWNED));
        studyMemberRepository.save(
            makeStudyMember(givenStudy, otherUser, StudyMemberStatus.ACCEPTED));
        // post
        givenPost = postRepository.save(makePost(GENERAL, givenStudy, givenUser));
        Post finishPost = postRepository.save(makePost(GENERAL, finishStudy, givenUser));
        // comment
        finishComment = commentRepository.save(makeComment(finishPost, givenUser));
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
        }

        @Test
        @DisplayName("S 댓글 작성자는 댓글을 삭제할 수 있다.")
        void deleteComment() {
            commentCommandService.deleteById(givenUser.getId(), comment.getId());
            assertThat(commentRepository.existsById(comment.getId())).isFalse();
        }

        @Test
        @DisplayName("S 스터디장은 댓글을 삭제할 수 있다")
        void ownedDeleteComment() {
            commentCommandService.deleteById(ownedUser.getId(), comment.getId());
            assertThat(commentRepository.existsById(comment.getId())).isFalse();
        }

        @Test
        @DisplayName("F 댓글이 존재하지 않는 경우 예외 발생")
        void commentNotFound() {
            Long userId = givenUser.getId();
            Long notExistCommentId = 0L;

            assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(
                    () -> commentCommandService.deleteById(userId, notExistCommentId)
                );
        }

        @Test
        @DisplayName("F 댓글 작성자나 관리자가 아니라면 예외 발생")
        void commentNoPermission() {
            Long otherUserId = otherUser.getId();
            Long commentId = comment.getId();

            assertThatExceptionOfType(CommentNoPermissionException.class)
                .isThrownBy(
                    () -> commentCommandService.deleteById(otherUserId, commentId)
                );
        }

        @Test
        @DisplayName("F 스터디가 종료되었을 경우 예외 발생")
        void finishedStudy() {
            Long userId = givenUser.getId();
            Long finishCommentId = finishComment.getId();

            assertThatExceptionOfType(FinishedStudyException.class)
                .isThrownBy(
                    () -> commentCommandService.deleteById(userId, finishCommentId)
                );
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
                givenPost.getId(),
                givenUser.getId(),
                request
            );
            Optional<Comment> want = commentRepository.findById(got);

            assertThat(want).isPresent();
            assertThat(want.get().getContent()).isEqualTo(request.content());
        }
    }

    @Nested
    @DisplayName("댓글 수정 #137")
    class EditCommentTest {

        Comment givenComment;

        User otherUser;

        Edit request;

        @BeforeEach
        void setUp() {
            givenComment = commentRepository.save(makeComment(givenPost, givenUser));
            otherUser = userRepository.save(makeUser());
            request = Edit.builder()
                .content("댓글 수정 테스트입니다.")
                .build();
        }

        @AfterEach
        void tearDown() {
            commentRepository.deleteAllInBatch();
            userRepository.deleteById(otherUser.getId());
        }

        @Test
        @DisplayName("S 댓글을 수정할 수 있다")
        void editComment() {
            commentCommandService.editComment(givenUser.getId(), givenComment.getId(), request);

            Comment editedComment = commentRepository.findById(givenComment.getId())
                .orElseThrow(CommentNotFoundException::new);
            assertThat(editedComment.getContent()).isEqualTo(request.content());
        }

        @Test
        @DisplayName("F 해당 댓글이 존재하지 않을 경우 예외 발생")
        void commentNotExist() {
            Long userId = givenUser.getId();
            Long wrongCommentId = 0L;

            assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(
                    () -> commentCommandService.editComment(userId, wrongCommentId, request)
                );
        }

        @Test
        @DisplayName("F 댓글 작성자가 현재 로그인 유저가 아닐 경우 예외 발생")
        void commentEditNotPermission() {
            Long otherUserId = otherUser.getId();
            Long commentId = givenComment.getId();

            assertThatExceptionOfType(CommentNoPermissionException.class)
                .isThrownBy(() ->
                    commentCommandService.editComment(
                        otherUserId,
                        commentId,
                        request
                    )
                );
        }

        @Test
        @DisplayName("F 현재 진행중인 스터디가 아닐 경우 예외 발생")
        void finishedStudy() {
            Long givenUserId = givenUser.getId();
            Long finishCommentId = finishComment.getId();
            assertThatExceptionOfType(FinishedStudyException.class)
                .isThrownBy(() ->
                    commentCommandService.editComment(
                        givenUserId,
                        finishCommentId,
                        request
                    )
                );
        }
    }

}
