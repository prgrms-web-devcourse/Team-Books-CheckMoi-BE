package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.PENDING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.IN_PROGRESS;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeStudyInfo;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudy;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyMember;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUser;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.repository.BookRepository;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
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
            pageRequest.setSize(4);
            int totalPage = 1;
            Page<StudyInfo> result =
                studyRepository.findRecruitingStudyByBookId(givenBook.getId(), pageRequest.of());

            assertThat(result.getContent()).hasSize(4);
            assertThat(result.getTotalPages()).isEqualTo(totalPage);
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
                studyRepository.getStudyDetailWithMembers(study.getId());
            validateStudyDetailInfo(response);
            validateMembers(response);

            assertThat(response.members()).hasSize(2);
        }

        private void validateStudyDetailInfo(StudyDetailWithMembers response) {
            assertAll(
                () -> assertThat(response.study()).hasFieldOrProperty("id"),
                () -> assertThat(response.study()).hasFieldOrProperty("name"),
                () -> assertThat(response.study()).hasFieldOrProperty("thumbnail"),
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
                () -> assertThat(response.members().get(0)).hasFieldOrProperty("image")
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

        private Study study1;

        private Study study2;

        private Study study3;

        private Study study4;

        private Study study5;

        @BeforeEach
        void setUp() {
            user = userRepository.save(makeUser());
            Book book = bookRepository.save(makeBook());

            study1 = studyRepository.save(makeStudy(book, RECRUITING));
            study2 = studyRepository.save(makeStudy(book, IN_PROGRESS));
            study3 = studyRepository.save(makeStudy(book, IN_PROGRESS));
            study4 = studyRepository.save(makeStudy(book, FINISHED));
            study5 = studyRepository.save(makeStudy(book, FINISHED));

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
        @DisplayName("S 내가 현재 참가중인 스터디 목록을 가져온다.")
        void getParticipationStudies() {
            Studies got = studyRepository.getParticipationStudies(user.getId());
            Studies want = new Studies(List.of(
                makeStudyInfo(study1),
                makeStudyInfo(study2),
                makeStudyInfo(study3)
            ), 0);

            assertThat(got.studies()).hasSize(want.studies().size());
            assertThat(got)
                .usingRecursiveComparison()
                .isEqualTo(want);
        }

        @Test
        @DisplayName("S 현재 종료된 참여한 스터디 목록을 가져온다.")
        void getFinishedStudies() {
            Studies got = studyRepository.getFinishedStudies(user.getId());
            Studies want = new Studies(List.of(
                makeStudyInfo(study4)
            ), 0);

            assertThat(got.studies()).hasSize(want.studies().size());
            assertThat(got)
                .usingRecursiveComparison()
                .isEqualTo(want);
        }

        @Test
        @DisplayName("S 내가 스터디장인 스터디 목록을 가져온다.")
        void getOwnedStudies() {
            Studies got = studyRepository.getOwnedStudies(user.getId());
            Studies want = new Studies(List.of(
                makeStudyInfo(study1)
            ), 0);

            assertThat(got.studies()).hasSize(want.studies().size());
            assertThat(got)
                .usingRecursiveComparison()
                .isEqualTo(want);
        }
    }

    @Nested
    @DisplayName("스터디 조회 v2 #158")
    class SearchStudies {

        private List<User> users = new ArrayList<>();

        private List<Study> studies = new ArrayList<>();

        private StudyConverter studyConverter = new StudyConverter();

        @BeforeEach
        void setUp() {
            Book book = bookRepository.save(makeBook());

            users.add(userRepository.save(makeUser()));
            users.add(userRepository.save(makeUser()));

            studies.add(studyRepository.save(makeStudy(book, RECRUITING)));
            studies.add(studyRepository.save(makeStudy(book, IN_PROGRESS)));
            studies.add(studyRepository.save(makeStudy(book, IN_PROGRESS)));
            studies.add(studyRepository.save(makeStudy(book, FINISHED)));
            studies.add(studyRepository.save(makeStudy(book, FINISHED)));

            // user 1 join
            studyMemberRepository.save(
                makeStudyMember(studies.get(0), users.get(0), OWNED));
            studyMemberRepository.save(
                makeStudyMember(studies.get(1), users.get(0), StudyMemberStatus.ACCEPTED));
            studyMemberRepository.save(
                makeStudyMember(studies.get(2), users.get(0), StudyMemberStatus.ACCEPTED));

            // user 2 join
            studyMemberRepository.save(
                makeStudyMember(studies.get(3), users.get(1), StudyMemberStatus.ACCEPTED));
            studyMemberRepository.save(
                makeStudyMember(studies.get(4), users.get(1), StudyMemberStatus.DENIED));
        }

        @AfterEach
        void tearDown() {
            studyMemberRepository.deleteAllInBatch();
            studyRepository.deleteAllInBatch();
            bookRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("S user 1번이 가입한 스터디를 찾을 수 있다")
        void searchStudies() {
            User givenUser = users.get(0);

            Search search = Search.builder()
                .userId(givenUser.getId())
                .build();
            PageRequest page = PageRequest.builder().build();

            Page<StudyInfo> result =
                studyRepository.findAllByCondition(givenUser.getId(), search, page.of());
            assertThat(result.getContent()).hasSize(3);
        }
    }
}

