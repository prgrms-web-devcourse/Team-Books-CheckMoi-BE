package com.devcourse.checkmoi.domain.comment.service;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.BOOK_REVIEW;
import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeComment;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePost;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.Assertions.assertThat;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.comment.converter.CommentConverter;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.checkmoi.domain.comment.repository.CommentRepository;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.IntegrationTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentQueryServiceImplTest extends IntegrationTest {

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
    private CommentQueryServiceImpl commentQueryService;

    @Autowired
    private CommentConverter commentConverter;

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
    @DisplayName("작성된 글에 대한 댓글 목록 조회 #130")
    class FindAllComments {

        User user1;

        User user2;

        User user3;

        Post givenPost;

        @BeforeEach
        void given() {
            Book book = bookRepository.save(makeBook());
            Study study = studyRepository.save(makeStudy(book, IN_PROGRESS));

            user1 = userRepository.save(makeUser());
            user2 = userRepository.save(makeUser());
            user3 = userRepository.save(makeUser());

            studyMemberRepository.save(makeStudyMember(study, user1, OWNED));
            studyMemberRepository.save(makeStudyMember(study, user2, ACCEPTED));
            studyMemberRepository.save(makeStudyMember(study, user3, ACCEPTED));

            givenPost = postRepository.save(makePost(BOOK_REVIEW, study, user1));
            Post noGivenPost = postRepository.save(makePost(GENERAL, study, user2));
            commentRepository.save(makeComment(noGivenPost, user2));

        }

        @Test
        @DisplayName("S 해당 포스트에 작성한 댓글을 조회할 수 있다")
        void findAllComments() {
            List<CommentInfo> commentInfos = Stream.of(
                commentRepository.save(makeComment(givenPost, user2)),
                commentRepository.save(makeComment(givenPost, user2)),
                commentRepository.save(makeComment(givenPost, user3))
            ).map(commentConverter::commentToInfo).toList();
            Long totalPage = 1L;
            Search search = Search.builder()
                .postId(givenPost.getId())
                .build();
            SimplePage simplePage = SimplePage.builder()
                .size(3)
                .page(1)
                .build();
            Comments comments =
                commentQueryService.findAllComments(search, simplePage.pageRequest());

            assertThat(comments.comments())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt")
                .hasSameElementsAs(commentInfos);
            assertThat(comments.totalPage())
                .isEqualTo(totalPage);
        }
    }
}
