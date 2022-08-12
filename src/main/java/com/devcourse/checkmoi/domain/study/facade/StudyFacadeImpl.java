package com.devcourse.checkmoi.domain.study.facade;

import com.devcourse.checkmoi.domain.book.service.BookQueryService;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudies;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyFacadeImpl implements StudyFacade {

    private final StudyQueryService studyQueryService;

    private final StudyCommandService studyCommandService;

    private final UserQueryService userQueryService;

    private final BookQueryService bookQueryService;

    @Override
    public MyStudies getMyStudies(Long userId) {
        return MyStudies.builder()
            .user(userQueryService.findUserInfo(userId))
            .participation(studyQueryService.getParticipationStudies(userId))
            .finished(studyQueryService.getFinishedStudies(userId))
            .owned(studyQueryService.getOwnedStudies(userId))
            .build();
    }

    @Override
    public Long createStudy(Create request, Long userId) {
        bookQueryService.getById(request.bookId());
        return studyCommandService.createStudy(request, userId);
    }
}
