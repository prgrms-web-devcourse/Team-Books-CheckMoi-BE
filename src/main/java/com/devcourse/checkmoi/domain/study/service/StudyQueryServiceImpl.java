package com.devcourse.checkmoi.domain.study.service;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.ExpiredStudies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyServiceValidator;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyQueryServiceImpl implements StudyQueryService {

    private final StudyServiceValidator studyValidator;

    private final StudyRepository studyRepository;

    private final StudyMemberRepository studyMemberRepository;


    @Override
    public Studies findAllByCondition(Long userId, Search search, Pageable pageable) {
        Page<StudyInfo> studyInfos = studyRepository.findAllByCondition(userId, search,
            pageable);
        return new Studies(
            studyInfos.getContent(),
            studyInfos.getTotalPages()
        );
    }

    @Override
    public Studies getStudies(Long bookId, Pageable pageable) {
        Page<StudyInfo> studyInfos = studyRepository.findRecruitingStudyByBookId(bookId, pageable);
        return new Studies(
            studyInfos.getContent(),
            studyInfos.getTotalPages()
        );
    }

    @Override
    public StudyDetailWithMembers getStudyInfoWithMembers(Long studyId) {
        return studyRepository.getStudyDetailWithMembers(studyId);
    }

    @Override
    public StudyAppliers getStudyAppliers(Long userId, Long studyId) {
        Long studyOwnerId = studyRepository.findStudyOwner(studyId);

        studyValidator.validateStudyOwner(userId, studyOwnerId,
            "스터디 신청 목록 조회 권한이 없습니다. 스터디 Id : " + studyId + "유저 Id : " + userId + " 스터디 장 Id : "
                + studyOwnerId
        );

        return studyRepository.getStudyApplicants(studyId);
    }

    @Override
    public Studies getParticipationStudies(Long userId) {
        return studyRepository.getParticipationStudies(userId);
    }

    @Override
    public Studies getFinishedStudies(Long userId) {
        return studyRepository.getFinishedStudies(userId);
    }

    @Override
    public Studies getOwnedStudies(Long userId) {
        return studyRepository.getOwnedStudies(userId);
    }

    @Override
    public void ongoingStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(StudyNotFoundException::new);
        studyValidator.ongoingStudy(study);
    }

    @Override
    public void participateUser(Long studyId, Long userId) {
        studyValidator.participateUser(
            studyMemberRepository.participateUserInStudy(studyId, userId));
    }

    @Override
    public ExpiredStudies getAllExpiredStudies(LocalDate criteriaTime, StudyStatus toStatus) {
        return studyRepository.getAllToBeProcessed(criteriaTime, toStatus);
    }
}
