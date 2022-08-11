package com.devcourse.checkmoi.domain.comment.repository;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeComment;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePost;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.Assertions.assertThat;
import com.devcourse.checkmoi.global.model.SimplePage;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.model.Comment;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.RepositoryTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    User user;

    Study study;

    Post post;

    @BeforeEach
    void setUp() {
        Book book = bookRepository.save(makeBook());
        user = userRepository.save(makeUser());
        study = studyRepository.save(makeStudy(book, StudyStatus.RECRUITING));
        post = postRepository.save(makePost(PostCategory.GENERAL, study, user));
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        studyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("댓글조회 페이지 네이션 #178")
    class GetCommentsPage {

        List<Comment> comments = new ArrayList<>();

        @BeforeEach
        void setComments() {
            for (int i = 0; i < 5; i++) {
                comments.add(commentRepository.save(makeComment(post, user)));
            }
        }

        @AfterEach
        void tearDownComments() {
            commentRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("S 댓글을 조회한다.")
        void getComments() {
            int size = 2;
            Search search = new Search(post.getId());
            SimplePage pageRequest = SimplePage.builder()
                .page(1)
                .size(size)
                .build();
            Pageable pageable = pageRequest.pageRequest();
            long totalPage = 3L;

            Page<CommentInfo> got = commentRepository.findAllByCondition(search,
                pageable);

            assertThat(got.getContent()).hasSize(size);
            assertThat(got.getTotalPages()).isEqualTo(totalPage);
        }
    }
}