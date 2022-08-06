package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.PENDING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.global.model.PageRequest;
import com.devcourse.checkmoi.template.RepositoryTest;
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
            User ownedUser = userRepository.saveAndFlush(makeUser());
            User memberUser = userRepository.saveAndFlush(makeUser());
            Book book = bookRepository.saveAndFlush(makeBook());
            Study study = studyRepository.saveAndFlush(makeStudy(book, IN_PROGRESS));

            studyMemberRepository.saveAndFlush(makeStudyMember(study, ownedUser, OWNED));
            studyMemberRepository.saveAndFlush(makeStudyMember(study, memberUser, ACCEPTED));

            Long got = studyRepository.findStudyOwner(study.getId());

            assertThat(got)
                .isEqualTo(ownedUser.getId());
        }

        @Test
        @DisplayName("S 해당 스터디에 대한 스터디 신청 중 PENDING 상태인 스터디 신청들을 모두 DENIED 상태로 변경한다")
        void denySuccess() {
            User owner = userRepository.save(makeUser());
            User pendingUser = userRepository.save(makeUser());
            User acceptedUser = userRepository.save(makeUser());

            Book book = bookRepository.save(makeBook());
            Study study = studyRepository.save(makeStudy(book, IN_PROGRESS));

            studyMemberRepository.save(makeStudyMember(study, owner, OWNED));
            studyMemberRepository.save(makeStudyMember(study, pendingUser, PENDING));
            studyMemberRepository.save(makeStudyMember(study, acceptedUser, ACCEPTED));

            studyRepository.updateAllAppliersAsDenied(study.getId());

            List<StudyUserInfo> appliers = studyRepository
                .getStudyAppliers(study.getId())
                .appliers();

            Assertions.assertThat(appliers)
                .isEmpty();
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
                Study study = studyRepository.save(makeStudy(givenBook, RECRUITING));
                studyMemberRepository.save(makeStudyMember(study, user, OWNED));
                studies.add(study);
            }
        }

        @Test
        @DisplayName("책 아이디를 기준으로 모집중인 스터디 정보를 조회한다.")
        void findRecruitingStudyByBookId() {
            PageRequest pageRequest = new PageRequest();

            List<Study> result =
                studyRepository.findRecruitingStudyByBookId(givenBook.getId(), pageRequest.of());

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
            User notStudyMemberUser = userRepository.save(makeUser());
            User studyMemberUser = userRepository.save(makeUser());
            Book book = bookRepository.save(makeBook());
            study = studyRepository.save(makeStudy(book, RECRUITING));

            studyMemberRepository.save(
                makeStudyMember(study, user, OWNED));
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

            assertThat(response.members().size()).isEqualTo(2);
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

        private User ownedUser;

        private User user1Pending;

        private User user2Pending;

        private User user3Accepted;

        @BeforeEach
        void setUp() {
            ownedUser = userRepository.save(makeUser());
            user1Pending = userRepository.save(makeUser());
            user2Pending = userRepository.save(makeUser());
            user3Accepted = userRepository.save(makeUser());
            Book book = bookRepository.save(makeBook());

            study = studyRepository.save(makeStudy(book, RECRUITING));

            studyMemberRepository.save(
                makeStudyMember(study, ownedUser, OWNED));
            studyMemberRepository.save(
                makeStudyMember(study, user1Pending, StudyMemberStatus.PENDING));
            studyMemberRepository.save(
                makeStudyMember(study, user2Pending, StudyMemberStatus.PENDING));
            studyMemberRepository.save(
                makeStudyMember(study, user3Accepted, StudyMemberStatus.ACCEPTED));

            userRepository.flush();
            studyMemberRepository.flush();
            studyRepository.flush();
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

            Assertions.assertThat(studyAppliers.appliers())
                .hasSize(2);
        }

        @Test
        @DisplayName("S 아직 수락, 거절 되지 않은 스터디 신청자 목록을 오래된순으로 가져온다")
        void getAllAppliersAscSuccess() {
            StudyAppliers studyAppliers = studyRepository.getStudyAppliers(study.getId());

            StudyUserInfo firstUserInfo = studyAppliers.appliers().get(0);

            Assertions.assertThat(firstUserInfo.id())
                .isEqualTo(user1Pending.getId());
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

    @Nested
    @DisplayName("나와 관련된 스터디 목록 조회하기 #116")
    class GetMyStudiesTest {

        private User user;

        @BeforeEach
        void setUp() {
            user = userRepository.save(makeUser());
            Book book = bookRepository.save(makeBook());

            Study study1 = studyRepository.save(makeStudy(book, RECRUITING));
            Study study2 = studyRepository.save(makeStudy(book, IN_PROGRESS));
            Study study3 = studyRepository.save(makeStudy(book, IN_PROGRESS));
            Study study4 = studyRepository.save(makeStudy(book, FINISHED));
            Study study5 = studyRepository.save(makeStudy(book, FINISHED));

            studyMemberRepository.save(
                makeStudyMember(study1, user, OWNED));
            studyMemberRepository.save(
                makeStudyMember(study2, user, StudyMemberStatus.ACCEPTED));
            studyMemberRepository.save(
                makeStudyMember(study3, user, StudyMemberStatus.ACCEPTED));
            studyMemberRepository.save(
                makeStudyMember(study4, user, StudyMemberStatus.ACCEPTED));
            studyMemberRepository.save(
                makeStudyMember(study5, user, StudyMemberStatus.DENIED));

            userRepository.flush();
            bookRepository.flush();
            studyMemberRepository.flush();
            studyRepository.flush();
        }

        @AfterEach
        void tearDown() {
            studyMemberRepository.deleteAllInBatch();
            studyRepository.deleteAllInBatch();
            bookRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("S 내 유저 정보와 내가 참가하거나 참가했던 스터디 목록을 불러온다")
        void getMyStudies() {
            studyRepository.getMyStudies(user.getId());
        }
    }
}

