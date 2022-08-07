package com.devcourse.checkmoi.domain.study.facade;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
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

    private static final int PARTICIPATION = 0;

    private static final int FINISHED = 1;

    private static final int OWNED = 2;

    private final StudyQueryService studyQueryService;

    private final UserQueryService userQueryService;

    @Override
    public MyStudies getMyStudies(Long userId) {
        return MyStudies.builder()
            .user(userQueryService.findUserInfo(userId))
            .participation(studyQueryService.getParticipationStudies(userId))
            .finished(studyQueryService.getFinishedStudies(userId))
            .owned(studyQueryService.getOwnedStudies(userId))
            .build();
    }
}
