package com.devcourse.checkmoi.domain.study.service.study;

import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyQueryServiceImpl implements StudyQueryService {

    private final StudyConverter studyConverter;

    private final StudyRepository studyRepository;

    @Override
    public Studies getStudies(Long bookId, Pageable pageable) {
        return new Studies(
            studyRepository.findRecruitingStudyByBookId(bookId, pageable)
                .map(studyConverter::studyToStudyInfo)
        );
    }

    @Override
    public StudyDetailWithMembers getStudyInfoWithMembers(Long studyId) {
        return studyRepository.getStudyInfoWithMembers(studyId);
    }
}
