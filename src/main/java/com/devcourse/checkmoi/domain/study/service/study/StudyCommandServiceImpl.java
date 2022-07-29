package com.devcourse.checkmoi.domain.study.service.study;

import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.CreateStudy;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyCommandServiceImpl implements
    StudyCommandService {

    private final StudyConverter studyConverter;

    private final StudyRepository studyRepository;

    @Override
    public Long createStudy(CreateStudy request) {
        return studyRepository.save(studyConverter.createToEntity(request)).getId();
    }

}
