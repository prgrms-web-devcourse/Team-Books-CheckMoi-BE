package com.devcourse.checkmoi.domain.study.facade;

import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeBookInfo;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeStudyInfo;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeUserInfo;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.exception.BookNotFoundException;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.service.BookQueryService;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinMaximumReachedException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.domain.study.service.validator.StudyValidator;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.service.UserQueryService;
import com.devcourse.checkmoi.global.model.SimplePage;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class StudyFacadeImplTest {

    @InjectMocks
    StudyFacadeImpl studyFacade;

    @Mock
    StudyQueryService studyQueryService;

    @Mock
    UserQueryService userQueryService;

    @Mock
    BookQueryService bookQueryService;

    @Mock
    StudyCommandService studyCommandService;

    @Mock
    StudyValidator studyValidator;

    @Nested
    @DisplayName("내 스터디 참여 현황 조회 #116")
    class GetMyStudiesTest {

        private final Book book = makeBookWithId(1L);

        private final Study study1 = makeStudyWithId(book, StudyStatus.IN_PROGRESS, 1L);

        private final Study study2 = makeStudyWithId(book, StudyStatus.FINISHED, 2L);

        private final Study study3 = makeStudyWithId(book, StudyStatus.RECRUITING, 3L);

        @Test
        @DisplayName("S 내가 참여한 스터디 목록을 조회한다.")
        void getMyStudies() {
            Long userId = 1L;
            UserInfo user = makeUserInfo();
            Studies participation = new Studies(
                List.of(makeStudyInfo(study1)), 1
            );
            Studies finished = new Studies(
                List.of(makeStudyInfo(study2)), 1
            );
            Studies owned = new Studies(
                List.of(makeStudyInfo(study3)), 1
            );
            given(userQueryService.findUserInfo(anyLong()))
                .willReturn(user);
            given(studyQueryService.getParticipationStudies(anyLong()))
                .willReturn(participation);
            given(studyQueryService.getFinishedStudies(anyLong()))
                .willReturn(finished);
            given(studyQueryService.getOwnedStudies(anyLong()))
                .willReturn(owned);

            MyStudies got = studyFacade.getMyStudies(userId);

            assertThat(got.user()).usingRecursiveComparison().isEqualTo(user);
            assertThat(got.participation()).usingRecursiveComparison().isEqualTo(participation);
            assertThat(got.finished()).usingRecursiveComparison().isEqualTo(finished);
            assertThat(got.owned()).usingRecursiveComparison().isEqualTo(owned);
        }
    }

    @Nested
    @DisplayName("스터디 생성 #204")
    class CreateStudyTest {

        private final Book book = makeBookWithId(1L);
        private final StudyRequest.Create request = StudyRequest.Create.builder()
            .bookId(1L)
            .name("스터디 이름")
            .thumbnail("https://adventure.co.kr/no-image-placeholder/")
            .description("스터디입니다")
            .maxParticipant(5)
            .gatherStartDate(LocalDate.now())
            .gatherEndDate(LocalDate.now())
            .studyStartDate(LocalDate.now())
            .studyEndDate(LocalDate.now())
            .build();
        private final Long userId = 1L;

        @Test
        @DisplayName("S 스터디 생성")
        void createStudy() {
            Long createdStudyId = 1L;
            BookInfo bookInfo = makeBookInfo(book);
            int joinStudies = 1;

            given(bookQueryService.getById(anyLong()))
                .willReturn(bookInfo);
            given(userQueryService.getUserJoinedStudies(anyLong()))
                .willReturn(joinStudies);
            given(studyCommandService.createStudy(any(Create.class), anyLong()))
                .willReturn(createdStudyId);

            Long got = studyFacade.createStudy(request, userId);

            assertThat(got).isEqualTo(createdStudyId);
        }

        @Test
        @DisplayName("F 책이 존재하지 않으면 예외 발생")
        void notFoundBook() {
            Long createdStudyId = 1L;
            given(bookQueryService.getById(anyLong()))
                .willThrow(BookNotFoundException.class);

            assertThatExceptionOfType(BookNotFoundException.class)
                .isThrownBy(() -> studyFacade.createStudy(request, userId));
        }

        @Test
        @DisplayName("F 스터디 참여 개수가 10개 이상이라면 예외 발생")
        void maximumJoinStudy() {
            BookInfo bookInfo = makeBookInfo(book);
            int maxJoinStudies = 10;

            given(bookQueryService.getById(anyLong()))
                .willReturn(bookInfo);
            given(userQueryService.getUserJoinedStudies(anyLong()))
                .willReturn(maxJoinStudies);
            doThrow(StudyJoinMaximumReachedException.class)
                .when(studyValidator)
                .validateMaximumJoinStudy(maxJoinStudies);

            assertThatExceptionOfType(StudyJoinMaximumReachedException.class)
                .isThrownBy(() -> studyFacade.createStudy(request, userId));
        }
    }

    @Nested
    @DisplayName("스터디 조회 #204")
    class GetStudiesTest {

        Book book = makeBookWithId(1L);

        @Test
        @DisplayName("S 스터디 조회")
        void createStudy() {
            Long totalPage = 1L;
            SimplePage simplePage = SimplePage.builder()
                .page(1)
                .size(2)
                .build();
            Studies want = new Studies(
                List.of(
                    makeStudyInfo(makeStudyWithId(makeBookWithId(1L), RECRUITING, 1L)),
                    makeStudyInfo(makeStudyWithId(makeBookWithId(1L), RECRUITING, 3L))
                ),
                totalPage
            );
            BookInfo bookInfo = makeBookInfo(book);
            given(bookQueryService.getById(anyLong()))
                .willReturn(bookInfo);
            given(studyQueryService.getStudies(anyLong(), any(Pageable.class)))
                .willReturn(want);

            Studies got = studyFacade.getStudies(book.getId(), simplePage.pageRequest());

            assertThat(got).usingRecursiveComparison().isEqualTo(want);
        }
    }

    @Nested
    @DisplayName("스터디 참가 신청 #223")
    class RequestStudyJoinTest {

        private final Long userId = 1L;

        private final Long studyId = 1L;

        @Test
        @DisplayName("S 스터디 참가 신청")
        void requestStudyJoin() {
            Long studyMemberId = 1L;
            int joinStudies = 5;
            given(userQueryService.getUserJoinedStudies(anyLong()))
                .willReturn(joinStudies);
            given(studyCommandService.requestStudyJoin(anyLong(), anyLong()))
                .willReturn(studyMemberId);

            Long got = studyFacade.requestStudyJoin(studyId, userId);

            then(studyValidator)
                .should(times(1))
                .validateMaximumJoinStudy(anyInt());
            assertThat(got).isEqualTo(studyMemberId);
        }

        @Test
        @DisplayName("F 스터디 참여 개수가 10개 이상이라면 예외 발생")
        void maximumJoinStudy() {
            int maxJoinStudies = 10;

            given(userQueryService.getUserJoinedStudies(anyLong()))
                .willReturn(maxJoinStudies);
            doThrow(StudyJoinMaximumReachedException.class)
                .when(studyValidator)
                .validateMaximumJoinStudy(maxJoinStudies);

            assertThatExceptionOfType(StudyJoinMaximumReachedException.class)
                .isThrownBy(() -> studyFacade.requestStudyJoin(studyId, userId));
        }
    }
}