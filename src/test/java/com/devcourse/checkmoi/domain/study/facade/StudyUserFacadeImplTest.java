package com.devcourse.checkmoi.domain.study.facade;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeMyStudies;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserInfo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.service.UserQueryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyUserFacadeImplTest {

    @InjectMocks
    StudyUserFacadeImpl studyUserFacade;

    @Mock
    StudyQueryService studyQueryService;

    @Mock
    UserQueryService userQueryService;

    @Nested
    @DisplayName("내 스터디 참여 현황 조회 #116")
    class GetMyStudiesTest {

        @Test
        @DisplayName("S 내가 참여한 스터디 목록을 조회한다.")
        void getMyStudies() {
            Long userId = 1L;
            UserInfo user = makeUserInfo();
            List<Studies> studies = makeMyStudies();
            given(userQueryService.findUserInfo(anyLong()))
                .willReturn(user);
            given(studyQueryService.getMyStudies(anyLong()))
                .willReturn(studies);

            MyStudies got = studyUserFacade.getMyStudies(userId);

            assertThat(got.user()).usingRecursiveComparison().isEqualTo(user);
            assertThat(got.participation()).usingRecursiveComparison().isEqualTo(studies.get(0));
            assertThat(got.finished()).usingRecursiveComparison().isEqualTo(studies.get(1));
            assertThat(got.owned()).usingRecursiveComparison().isEqualTo(studies.get(2));
        }
    }
}