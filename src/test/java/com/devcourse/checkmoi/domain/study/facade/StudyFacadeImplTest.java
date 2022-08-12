package com.devcourse.checkmoi.domain.study.facade;

import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeBookInfo;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeStudyInfo;
import static com.devcourse.checkmoi.util.DTOGeneratorUtil.makeUserInfo;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.service.BookQueryService;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.service.UserQueryService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        Book book = makeBookWithId(1L);

        @Test
        @DisplayName("S 스터디 생성")
        void createStudy() {
            Long userId = 1L;
            StudyRequest.Create request = StudyRequest.Create.builder()
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
            Long createdStudyId = 1L;
            BookInfo bookInfo = makeBookInfo(book);
            given(bookQueryService.getById(anyLong()))
                .willReturn(bookInfo);
            given(studyCommandService.createStudy(any(Create.class), anyLong()))
                .willReturn(createdStudyId);

            Long got = studyFacade.createStudy(request, userId);

            assertThat(got).isEqualTo(createdStudyId);
        }
    }
}