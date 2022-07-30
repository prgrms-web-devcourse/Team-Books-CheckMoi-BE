package com.devcourse.checkmoi.domain.study.service.study;

import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.study.StudyRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyCommandServiceImpl implements StudyCommandService {

    private final StudyConverter studyConverter;

    private final StudyRepository studyRepository;

    @Override
    public Long createStudy(Create request) {
        return studyRepository
            .save(studyConverter.createToEntity(request))
            .getId();
    }

    @Override
    public Long editStudyInfo(Long studyId, Edit request) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        study.editName(request.name());
        study.editThumbnail(request.thumbnail());
        study.editDescription(request.description());
        return study.getId();
    }

}
