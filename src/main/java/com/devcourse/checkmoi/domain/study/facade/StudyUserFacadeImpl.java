package com.devcourse.checkmoi.domain.study.facade;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudyInfo;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.service.UserQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyUserFacadeImpl implements
    StudyUserFacade {

    private static final int IN_PROGRESS = 0;

    private static final int FINISHED = 1;

    private static final int OWNED = 2;

    private final StudyQueryService studyQueryService;

    private final UserQueryService userQueryService;

    @Override
    public MyStudies getMyStudies(Long userId) {
        UserInfo user = userQueryService.findUserInfo(userId);
        List<List<MyStudyInfo>> studies = studyQueryService.getMyStudies(userId);

        return MyStudies.builder()
            .user(user)
            .progress(studies.get(IN_PROGRESS))
            .finished(studies.get(FINISHED))
            .owned(studies.get(OWNED))
            .build();
    }
}
