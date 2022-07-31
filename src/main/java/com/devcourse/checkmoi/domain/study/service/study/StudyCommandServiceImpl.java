package com.devcourse.checkmoi.domain.study.service.study;

import static com.devcourse.checkmoi.global.exception.ErrorMessage.ACCESS_DENIED;
import static com.devcourse.checkmoi.global.exception.ErrorMessage.STUDY_JOIN_REQUEST_NOT_FOUND;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.exception.NotStudyOwnerException;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinRequestNotFoundException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.repository.study.StudyMemberRepository;
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

    private final StudyMemberRepository studyMemberRepository;

    @Override
    public Long createStudy(Create request) {
        return studyRepository
            .save(studyConverter.createToEntity(request))
            .getId();
    }

    @Override
    public Long editStudyInfo(Long studyId, Long userId, Edit request) {
        validateExistStudy(studyRepository.existsById(studyId));
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);
        validateStudyOwner(userId, studyOwnerId,
            "스터디 정보 수정 권한이 없습니다. 유저 Id : " + userId + " 스터디장 Id : " + studyOwnerId);
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        study.editName(request.name());
        study.editThumbnail(request.thumbnail());
        study.editDescription(request.description());
        return study.getId();
    }

    @Override
    public void auditStudyParticipation(Long studyId, Long memberId, Long userId, Audit request) {
        validateExistStudy(studyRepository.existsById(studyId));
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);
        validateStudyOwner(userId, studyOwnerId,
            "스터디 승인 권한이 없습니다. 유저 Id : " + userId + " 스터디 장 Id : " + studyOwnerId
        );
        StudyMember studyMember = studyMemberRepository.findById(memberId)
            .orElseThrow(() -> new StudyJoinRequestNotFoundException(STUDY_JOIN_REQUEST_NOT_FOUND));
        studyMember.changeStatus(StudyMemberStatus.valueOf(request.status()));
    }

    private void validateExistStudy(boolean existStudy) {
        if (!existStudy) {
            throw new StudyNotFoundException();
        }
    }

    private void validateStudyOwner(Long userId, Long studyOwnerId, String message) {
        if (!studyOwnerId.equals(userId)) {
            throw new NotStudyOwnerException(message, ACCESS_DENIED);
        }
    }

}
