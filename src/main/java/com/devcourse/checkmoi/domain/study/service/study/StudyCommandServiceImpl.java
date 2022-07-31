package com.devcourse.checkmoi.domain.study.service.study;

import static com.devcourse.checkmoi.global.exception.ErrorMessage.ACCESS_DENIED;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
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
    public Long editStudyInfo(Long studyId, Long userId, Edit request) {
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);
        validateStudyOwner(userId, studyOwnerId);
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        study.editName(request.name());
        study.editThumbnail(request.thumbnail());
        study.editDescription(request.description());
        return study.getId();
    }

    private void validateStudyOwner(Long userId, Long studyOwnerId) {
        if (!studyOwnerId.equals(userId)) {
            throw new NotStudyOwnerException(
                "스터디 정보 수정 권한이 없습니다. 유저 아이디 : " + userId + " 스터디장 Id : " + studyOwnerId, ACCESS_DENIED);
        }
    }
}
