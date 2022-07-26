package com.devcourse.checkmoi.domain.study.service;

import static com.devcourse.checkmoi.global.exception.error.ErrorMessage.STUDY_JOIN_REQUEST_NOT_FOUND;
import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.exception.StudyJoinRequestNotFoundException;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyValidator;
import com.devcourse.checkmoi.domain.user.exception.UserNotFoundException;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final StudyValidator studyValidator;

    @Override
    public Long createStudy(Create request, Long userId) {
        Study study = studyRepository.save(studyConverter.createToEntity(request));
        StudyMember studyMember = StudyMember.builder()
            .user(User.builder()
                .id(userId)
                .build()
            )
            .study(study)
            .status(StudyMemberStatus.OWNED)
            .build();

        studyMemberRepository.save(studyMember);
        return study.getId();
    }

    @Override
    public Long editStudyInfo(Long studyId, Long userId, Edit request) {
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);
        studyValidator.validateStudyOwner(userId, studyOwnerId,
            "스터디 정보 수정 권한이 없습니다. 유저 Id : " + userId + " 스터디장 Id : " + studyOwnerId);
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        StudyStatus beforeStatus = study.getStatus();

        study.editName(request.name());
        study.editThumbnail(request.thumbnail());
        study.editDescription(request.description());
        study.changeStatus(StudyStatus.nameOf(request.status()));

        if (isNecessaryToDeny(beforeStatus, study.getStatus())) {
            studyRepository.updateAllApplicantsAsDenied(studyId);
        }

        return study.getId();
    }

    private boolean isNecessaryToDeny(StudyStatus beforeStatus, StudyStatus afterStatus) {
        return beforeStatus == StudyStatus.RECRUITING &&
            afterStatus == StudyStatus.IN_PROGRESS;
    }

    @Override
    public void auditStudyParticipation(Long studyId, Long memberId, Long userId, Audit request) {
        StudyMemberStatus changeStatus = StudyMemberStatus.valueOf(request.status().toUpperCase());
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        studyValidator.validateRecruitingStudy(study);
        studyValidator.validateFullMemberStudy(study);
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);
        studyValidator.validateStudyOwner(userId, studyOwnerId,
            "스터디 승인 권한이 없습니다. 유저 Id : " + userId + " 스터디 장 Id : " + studyOwnerId
        );
        StudyMember studyMember = studyMemberRepository.findById(memberId)
            .orElseThrow(() -> new StudyJoinRequestNotFoundException(STUDY_JOIN_REQUEST_NOT_FOUND));
        if (changeStatus == StudyMemberStatus.ACCEPTED) {
            int joinStudy = userRepository.userJoinedStudies(studyMember.getUser().getId());
            studyValidator.validateMaximumJoinStudy(joinStudy);
        }
        studyMember.changeStatus(changeStatus);
    }

    @Override
    public Long requestStudyJoin(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        studyValidator.validateRecruitingStudy(study);
        studyValidator.validateFullMemberStudy(study);
        StudyMember request = studyMemberRepository.findByUserAndStudy(userId, studyId)
            .map(studyMember -> {
                studyValidator.validateDuplicateStudyMemberRequest(studyMember);
                studyMember.changeStatus(StudyMemberStatus.PENDING);
                return studyMember;
            })
            .orElseGet(() ->
                StudyMember.builder()
                    .study(study)
                    .user(User.builder().id(userId).build())
                    .status(StudyMemberStatus.PENDING)
                    .build()
            );

        return studyMemberRepository.save(request).getId();
    }

    @Override
    public void updateStudyStatus(Long studyId, StudyStatus toStatus) {
        studyRepository.updateStudyStatus(studyId, toStatus);
    }

    @Override
    public void updateApplicants(Long studyId, StudyMemberStatus toMemberStatus) {
        if (toMemberStatus.equals(StudyMemberStatus.DENIED)) {
            studyRepository.updateAllApplicantsAsDenied(studyId);
        }
    }
}
