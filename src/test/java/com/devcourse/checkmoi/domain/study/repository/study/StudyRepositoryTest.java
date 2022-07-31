package com.devcourse.checkmoi.domain.study.repository.study;

import static com.devcourse.checkmoi.domain.user.model.UserRole.GUEST;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.book.stub.BookStub;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.stub.StudyMemberStub;
import com.devcourse.checkmoi.domain.study.stub.StudyStub;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.domain.user.stub.UserStub;
import com.devcourse.checkmoi.global.model.PageRequest;
import com.devcourse.checkmoi.template.RepositoryTest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class StudyRepositoryTest extends RepositoryTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Nested
    @DisplayName("스터디 수정 #30")
    class findStudyOwnerTest {

        @Test
        @DisplayName("해당 스터디의 스터디장의 ID를 찾는다")
        void test() {
            String name = "name";
            User user = userRepository.saveAndFlush(
                User.builder().oauthId(name).provider("kakao").name(name)
                    .email(new Email(name + "@gmail.com")).userRole(GUEST).profileImgUrl("url")
                    .build()
            );
            Study study = studyRepository.saveAndFlush(
                Study.builder()
                    .id(1L)
                    .build()
            );
            StudyMember studyMember = studyMemberRepository.saveAndFlush(
                StudyMember.builder()
                    .id(1L)
                    .status(StudyMemberStatus.OWNED)
                    .study(study)
                    .user(
                        user
                    )
                    .build()
            );
            Long got = studyRepository.findStudyOwner(study.getId());

            assertThat(got)
                .isEqualTo(studyMember.getUser().getId());
        }
    }

    @Nested
    @DisplayName("특정 책에 대한 스터디 목록 조회 #43")
    class getStudies {

        @PersistenceContext
        private EntityManager entityManager;


        @BeforeEach
        void init() {
            entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
            List<Book> books = BookStub.StubBook();
            List<Study> studies = StudyStub.studiesStub();

            userRepository.saveAll(UserStub.usersStub());
            bookRepository.saveAll(books);
            studyRepository.saveAll(studies);
            studyMemberRepository.saveAll(StudyMemberStub.studyMembers());
        }

        @Test
        @DisplayName("책 아이디를 기준으로 모집중인 스터디 정보를 조회한다.")
        void findRecruitingStudyByBookId() {
            PageRequest pageRequest = new PageRequest();
            Pageable pageable = pageRequest.of();
            Page<Study> got = studyRepository.findRecruitingStudyByBookId(1L, pageable);
            List<String> want = StudyStub.javaRecrutingStudyNameStub();

            assertThat(got).hasSize(want.size());
            for (int i = 0; i < got.getSize(); i++) {
                assertThat(got.getContent().get(i).getName()).isEqualTo(want.get(i));
            }
        }
    }
}