package com.devcourse.checkmoi.domain.post.service;

import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makePost;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.exception.PostNoPermissionException;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.post.repository.PostRepository;
import com.devcourse.checkmoi.domain.study.exception.ClosedStudyException;
import com.devcourse.checkmoi.domain.study.exception.NotJoinedMemberException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostCommandServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommandService postService;

    private User owner;

    private Study study;

    private User member;

    private Book book;

    @BeforeEach
    void setUp() {
        book = bookRepository.save(makeBook());

        owner = userRepository.save(makeUser());
        member = userRepository.save(makeUser());

        study = studyRepository.save(makeStudy(book, IN_PROGRESS));

        studyMemberRepository.save(makeStudyMember(study, owner, StudyMemberStatus.OWNED));
        studyMemberRepository.save(makeStudyMember(study, member, StudyMemberStatus.ACCEPTED));
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        studyMemberRepository.deleteAllInBatch();
        studyRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreateTest {

        private User nonMember;


        private Create validCreateRequest;

        @BeforeEach
        void setUp() {
            nonMember = userRepository.save(makeUser());

            studyMemberRepository.save(makeStudyMember(study, nonMember, StudyMemberStatus.DENIED));

            validCreateRequest =
                Create.builder()
                    .title("abc")
                    .content("content")
                    .category("GENERAL")
                    .studyId(study.getId()).build();
        }

        @Test
        @DisplayName("F 스터디 참여 유저가 아니면 게시글 작성이 불가하다")
        void createFail() {
            Long nonMemberId = nonMember.getId();

            Assertions.assertThatThrownBy(
                    () -> postService.createPost(nonMemberId, validCreateRequest))
                .isInstanceOf(NotJoinedMemberException.class);

        }

        @Test
        @DisplayName("S 진행 완료 스터디에서 자유게시글을 작성할 수 있다")
        void createGeneral() {
            Long memberId = member.getId();

            Assertions.assertThatNoException()
                .isThrownBy(() ->
                    postService.createPost(memberId, validCreateRequest));
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class EditTest {

        private Study finishedStudy;

        private Edit editRequest;

        private Post notice;

        private Post general;

        @BeforeEach
        void setUp() {
            finishedStudy = studyRepository.save(makeStudy(book, FINISHED));

            studyMemberRepository.save(
                makeStudyMember(finishedStudy, member, StudyMemberStatus.ACCEPTED));

            notice = postRepository.save(makePost(PostCategory.NOTICE, finishedStudy, member));
            general = postRepository.save(makePost(PostCategory.GENERAL, finishedStudy, member));

            editRequest = Edit.builder()
                .content("modified")
                .title("modified title")
                .studyId(this.finishedStudy.getId())
                .build();
        }

        @Test
        @DisplayName("F 스터디 상태가 FINISHED 이후에는 자유게시글 외에는 수정을 할 수 없다")
        void editFail() {
            Long memberId = member.getId();
            Long noticeId = notice.getId();

            Assertions.assertThatThrownBy(() ->
                    postService.editPost(memberId, noticeId, editRequest))
                .isInstanceOf(ClosedStudyException.class);
        }

        @Test
        @DisplayName("S 스터디 상태가 FINISHED 이후에도 자유게시글은 수정할 수 있다")
        void edit() {
            Long generalPostId = general.getId();

            postService.editPost(member.getId(), general.getId(), editRequest);

            Post foundPost = postRepository.findById(generalPostId).get();

            Assertions.assertThat(foundPost.getContent())
                .isEqualTo(editRequest.content());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class DeleteTest {

        private User otherMember;

        @BeforeEach
        void setUp() {
            otherMember = userRepository.save(makeUser());

            studyMemberRepository.save(
                makeStudyMember(study, otherMember, StudyMemberStatus.ACCEPTED));
        }

        @Test
        @DisplayName("S 스터디장은 자신의 게시글이 아닌글도 삭제할 수 있다")
        void deleteByLeader() {
            Post memberPost = postRepository.save(makePost(PostCategory.GENERAL, study, member));

            postService.deletePost(owner.getId(), memberPost.getId());

            Optional<Post> optionalPost = postRepository.findById(memberPost.getId());

            Assertions.assertThat(optionalPost)
                .isEmpty();
        }

        @Test
        @DisplayName("F 스터디장이 아닌 일반 멤버는 자신의 게시글이 아닌글을 삭제 할 수 없다")
        void deleteFail() {
            Post memberPost = postRepository.save(makePost(PostCategory.GENERAL, study, member));
            Long otherMemberId = otherMember.getId();
            Long memberPostId = memberPost.getId();

            Assertions.assertThatThrownBy(() ->
                    postService.deletePost(otherMemberId, memberPostId))
                .isInstanceOf(PostNoPermissionException.class);
        }
    }
}
