package com.devcourse.checkmoi.domain.post.repository;

import static com.devcourse.checkmoi.domain.post.model.PostCategory.BOOK_REVIEW;
import static com.devcourse.checkmoi.domain.post.model.PostCategory.GENERAL;
import static com.devcourse.checkmoi.domain.post.model.PostCategory.NOTICE;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePost;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.Assertions.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.post.converter.PostConverter;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

class CustomPostRepositoryImplTest extends RepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;


    @Nested
    @DisplayName("게시글을 다중 조회할 수 있다 #86")
    class FindAllPostsTest {

        private User user;

        private Study study;

        private Post post;

        private PostConverter postConverter = new PostConverter();

        @BeforeEach
        void setGiven() {
            Book book = bookRepository.save(makeBook());
            // user
            User user2 = userRepository.save(makeUser());
            user = userRepository.save(makeUser());

            // study
            Study illegalStudy = studyRepository.save(makeStudy(book, FINISHED));
            study = studyRepository.save(makeStudy(book, IN_PROGRESS));

            // studyMember
            studyMemberRepository.save(makeStudyMember(study, user, StudyMemberStatus.OWNED));
            studyMemberRepository.save(makeStudyMember(study, user2, StudyMemberStatus.ACCEPTED));

            // given post
            post = postRepository.save(makePost(GENERAL, study, user));
            post = postRepository.save(makePost(NOTICE, study, user));
            post = postRepository.save(makePost(NOTICE, study, user));
            post = postRepository.save(makePost(NOTICE, study, user));
            post = postRepository.save(makePost(NOTICE, study, user));

            // illegal post
            postRepository.save(makePost(NOTICE, illegalStudy, user));
            postRepository.save(makePost(BOOK_REVIEW, illegalStudy, user));
        }

        @Test
        @DisplayName("S 게시글을 카테고리에 따라 검색할 수 있다-GENERAL")
        void findAllByCategoryGENERALCondition() {
            Search search = Search.builder()
                .studyId(study.getId())
                .category("GENERAL")
                .build();

            Page<PostResponse.PostInfo> posts = postRepository.findAllByCondition(user.getId(),
                search,
                PageRequest.of(0, 2, Sort.by(new Order(Direction.ASC, "createdAt"))));

            assertThat(posts.getTotalPages())
                .isEqualTo(1);
        }

        @Test
        @DisplayName("S 게시글을 카테고리에 따라 검색할 수 있다-NOTICE")
        void findAllByCategoryNOTICECondition() {
            Search search = Search.builder()
                .studyId(study.getId())
                .category("NOTICE")
                .build();

            Page<PostResponse.PostInfo> posts = postRepository.findAllByCondition(user.getId(),
                search,
                PageRequest.of(0, 2, Sort.by(new Order(Direction.ASC, "createdAt"))));

            assertThat(posts.getTotalPages())
                .isEqualTo(2);
        }

        @Test
        @DisplayName("S 전체 게시글을 검색할 수 있다")
        void findAllByCondition() {
            Search search = Search.builder()
                .studyId(study.getId())
                .build();

            Page<PostResponse.PostInfo> posts = postRepository.findAllByCondition(user.getId(),
                search,
                PageRequest.of(0, 2, Sort.by(new Order(Direction.ASC, "createdAt"))));

            assertThat(posts.getTotalPages())
                .isEqualTo(3);
        }
    }

}
