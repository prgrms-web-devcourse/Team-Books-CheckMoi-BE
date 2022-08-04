package com.devcourse.checkmoi.domain.study.repository.study;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeNonStudyMemberUser;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMemberUser;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.UserRole;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.model.PageRequest;
import com.devcourse.checkmoi.template.RepositoryTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    class FindStudyOwnerTest {

        @Test
        @DisplayName("해당 스터디의 스터디장의 ID를 찾는다")
        void test() {
            String name = "name";
            User user = userRepository.saveAndFlush(
                User.builder().oauthId(name).provider("kakao").name(name)
                    .email(new Email(name + "@gmail.com")).userRole(UserRole.LOGIN)
                    .profileImgUrl("url")
                    .build());
            Study study = studyRepository.saveAndFlush(
                Study.builder()
                    .id(1L)
                    .build());
            StudyMember studyMember = studyMemberRepository.saveAndFlush(
                StudyMember.builder()
                    .id(1L)
                    .status(StudyMemberStatus.OWNED)
                    .study(study)
                    .user(user)
                    .build());
            Long got = studyRepository.findStudyOwner(study.getId());

            assertThat(got)
                .isEqualTo(studyMember.getUser().getId());
        }

        @Test
        @DisplayName("S 해당 스터디에 대한 스터디 신청 중 PENDING 상태인 스터디 신청들을 모두 DENIED 상태로 변경한다")
        void denySuccess() {
            String name = "name";
            User owner = userRepository.saveAndFlush(
                User.builder()
                    .oauthId(name)
                    .provider("kakao")
                    .name(name)
                    .email(new Email(name + "@gmail.com"))
                    .userRole(UserRole.LOGIN)
                    .profileImgUrl("url")
                    .build());
            User studyMemberUser = userRepository.saveAndFlush(
                User.builder()
                    .oauthId(name)
                    .provider("kakao")
                    .name(name)
                    .email(new Email(name + 1 + "@gmail.com"))
                    .userRole(UserRole.LOGIN)
                    .profileImgUrl("url")
                    .build());
            Study study = studyRepository.saveAndFlush(
                Study.builder()
                    .id(1L)
                    .build());

            studyMemberRepository.saveAndFlush(
                StudyMember.builder()
                    .id(1L)
                    .status(StudyMemberStatus.OWNED)
                    .study(study)
                    .user(owner)
                    .build());
            studyMemberRepository.saveAndFlush(
                StudyMember.builder()
                    .id(1L)
                    .status(StudyMemberStatus.OWNED)
                    .study(study)
                    .user(studyMemberUser)
                    .build());
            studyRepository.updateAllAppliersAsDenied(study.getId());

            List<UserInfo> appliers = studyRepository.getStudyAppliers(study.getId())
                .appliers();

            Assertions.assertThat(appliers.size())
                .isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("특정 책에 대한 스터디 목록 조회 #43")
    class GetStudies {

        List<Study> studies = new ArrayList<>();

        Book givenBook;

        @BeforeEach
        void init() {
            User user = userRepository.save(makeUser());
            givenBook = bookRepository.save(makeBook());

            int generatedStudyNumber = 4;
            for (int i = 0; i < generatedStudyNumber; i++) {
                Study study = studyRepository.save(makeStudy(givenBook));
                studyMemberRepository.save(makeStudyMember(study, user, StudyMemberStatus.OWNED));
                studies.add(study);
            }
        }

        @Test
        @DisplayName("책 아이디를 기준으로 모집중인 스터디 정보를 조회한다.")
        void findRecruitingStudyByBookId() {
            PageRequest pageRequest = new PageRequest();

            Page<Study> pageResult =
                studyRepository.findRecruitingStudyByBookId(givenBook.getId(), pageRequest.of());

            List<Study> result = pageResult.getContent();

            assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .hasSameElementsAs(studies);
        }
    }

    @Nested
    @DisplayName("스터디 상세 조회 #56")
    class GetDetail {

        Study study;

        @BeforeEach
        private void setUpGiven() {
            User user = userRepository.save(makeUser());
            User notStudyMemberUser = userRepository.save(makeNonStudyMemberUser());
            User studyMemberUser = userRepository.save(makeStudyMemberUser());
            Book book = bookRepository.save(makeBook());
            study = studyRepository.save(makeStudy(book));
            studyMemberRepository.save(makeStudyMember(study, user, StudyMemberStatus.OWNED));
            studyMemberRepository.save(
                makeStudyMember(study, notStudyMemberUser, StudyMemberStatus.DENIED));
            studyMemberRepository.save(
                makeStudyMember(study, studyMemberUser, StudyMemberStatus.ACCEPTED));

        }

        @Test
        @DisplayName("S 스터디와 관련된 책과 스터디멤버 정보를 같이 조회할 수 있다")
        void getStudyInfoWithBookAndMembers() {
            StudyDetailWithMembers response =
                studyRepository.getStudyInfoWithMembers(study.getId());

            validateStudyDetailInfo(response);
            validateMembers(response);

            Assertions.assertThat(response.members().size())
                .isEqualTo(2);
        }

        private void validateStudyDetailInfo(StudyDetailWithMembers response) {
            assertAll(
                () -> assertThat(response.study()).hasFieldOrProperty("id"),
                () -> assertThat(response.study()).hasFieldOrProperty("name"),
                () -> assertThat(response.study()).hasFieldOrProperty("thumbnailUrl"),
                () -> assertThat(response.study()).hasFieldOrProperty("description"),
                () -> assertThat(response.study()).hasFieldOrProperty("currentParticipant"),
                () -> assertThat(response.study()).hasFieldOrProperty("maxParticipant"),
                () -> assertThat(response.study()).hasFieldOrProperty("currentParticipant"),
                () -> assertThat(response.study()).hasFieldOrProperty("gatherStartDate"),
                () -> assertThat(response.study()).hasFieldOrProperty("gatherEndDate"),
                () -> assertThat(response.study()).hasFieldOrProperty("studyStartDate"),
                () -> assertThat(response.study()).hasFieldOrProperty("studyEndDate")
            );
        }

        private void validateMembers(StudyDetailWithMembers response) {
            assertAll(
                () -> assertThat(response.members().get(0)).hasFieldOrProperty("id"),
                () -> assertThat(response.members().get(0)).hasFieldOrProperty("name"),
                () -> assertThat(response.members().get(0)).hasFieldOrProperty("email"),
                () -> assertThat(response.members().get(0)).hasFieldOrProperty("profileImageUrl")
            );
        }
    }

    @Nested
    @DisplayName("스터디 신청 목록 가져오기")
    class GetStudyAppliersTest {

        private Study study;

        private User ownerUser;

        private User firstAppliedButNotAcceptedUser;

        private User secondAppliedButNotAcceptedUser;

        private User studyMemberUser;

        @BeforeEach
        void setUp() {
            ownerUser = userRepository.save(User.builder()
                .oauthId("ASDASDQWDAASDZFWEF1")
                .provider("KAKAO")
                .name("카일")
                .temperature(36.5f)
                .email(new Email("khyle@test.com"))
                .profileImgUrl("https://example.com/java.png")
                .userRole(UserRole.LOGIN)
                .build());

            firstAppliedButNotAcceptedUser = userRepository.save(
                User.builder()
                    .oauthId("ASDASDQWDAASDZFWEF2")
                    .provider("KAKAO")
                    .name("거절당한_에밀리")
                    .temperature(36.5f)
                    .email(new Email("emily@test.com"))
                    .profileImgUrl("https://example.com/java.png")
                    .userRole(UserRole.LOGIN)
                    .build()
            );
            secondAppliedButNotAcceptedUser = userRepository.save(
                User.builder()
                    .oauthId("ASDASDQWDAASDZFWEF3")
                    .provider("KAKAO")
                    .name("거절당한 톰슨")
                    .temperature(36.5f)
                    .email(new Email("thompson@test.com"))
                    .profileImgUrl("https://example.com/java.png")
                    .userRole(UserRole.LOGIN)
                    .build()
            );

            studyMemberUser = userRepository.save(User.builder()
                .oauthId("ASDASDQWDAASDZFWEF4")
                .provider("KAKAO")
                .name("그레이스")
                .temperature(36.5f)
                .email(new Email("grace@test.com"))
                .profileImgUrl("https://example.com/java.png")
                .userRole(UserRole.LOGIN)
                .build()
            );

            Book book = bookRepository.save(Book.builder()
                .title("대왕고래")
                .description("대왕고래는 진짜 크다")
                .author("김자바")
                .publisher("자바출판")
                .isbn("1234123412341")
                .thumbnail("https://example.com/java.png")
                .publishedAt(new PublishedDate("20121111"))
                .build());

            study = studyRepository.save(Study.builder()
                .name("스터디-대왕고래책")
                .thumbnailUrl("https://example.com/java.png")
                .description("대왕고래 스터디")
                .maxParticipant(3)
                .status(StudyStatus.RECRUITING)
                .book(book)
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build());

            studyMemberRepository.save(makeStudyMember(study, ownerUser, StudyMemberStatus.OWNED));
            studyMemberRepository.save(
                makeStudyMember(study, firstAppliedButNotAcceptedUser, StudyMemberStatus.PENDING));
            studyMemberRepository.save(
                makeStudyMember(study, secondAppliedButNotAcceptedUser, StudyMemberStatus.PENDING));
            studyMemberRepository.save(
                makeStudyMember(study, studyMemberUser, StudyMemberStatus.ACCEPTED));
        }

        @AfterEach
        void tearDown() {
            studyMemberRepository.deleteAllInBatch();
            studyRepository.deleteAllInBatch();
            bookRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("S 아직 수락, 거절 되지 않은 스터디 신청자 목록을 가져온다")
        void getAllAppliersSuccess() {
            StudyAppliers studyAppliers = studyRepository.getStudyAppliers(study.getId());

            Assertions.assertThat(studyAppliers.appliers().size())
                .isEqualTo(2);
        }

        @Test
        @DisplayName("S 아직 수락, 거절 되지 않은 스터디 신청자 목록을 오래된순으로 가져온다")
        void getAllAppliersAscSuccess() {
            StudyAppliers studyAppliers = studyRepository.getStudyAppliers(study.getId());

            UserInfo firstUserInfo = studyAppliers.appliers().get(0);

            Assertions.assertThat(firstUserInfo.id())
                .isEqualTo(firstAppliedButNotAcceptedUser.getId());
        }

        @Test
        @DisplayName("S 스터디의 스터디원 수를 가져온다")
        @Transactional(propagation = Propagation.NEVER)
        void countSuccess() {
            Study foundStudy = studyRepository.findById(this.study.getId()).get();

            Assertions.assertThat(foundStudy.getCurrentParticipant())
                .isEqualTo(2);
        }
    }

}

