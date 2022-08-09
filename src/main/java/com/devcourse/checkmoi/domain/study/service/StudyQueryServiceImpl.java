package com.devcourse.checkmoi.domain.study.service;

import com.devcourse.checkmoi.domain.study.converter.StudyConverter;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.exception.StudyNotFoundException;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.repository.StudyMemberRepository;
import com.devcourse.checkmoi.domain.study.repository.StudyRepository;
import com.devcourse.checkmoi.domain.study.service.validator.StudyServiceValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyQueryServiceImpl implements StudyQueryService {

    private final StudyConverter studyConverter;

    private final StudyServiceValidator studyValidator;

    private final StudyRepository studyRepository;

    private final StudyMemberRepository studyMemberRepository;


    @Override
    public List<StudyInfo> findAllByCondition(Long userId, Search search, Pageable pageable) {
        return studyRepository.findAllByCondition(userId, search, pageable);
    }

    @Override
    public Studies getStudies(Long bookId, Pageable pageable) {
        return new Studies(
            studyRepository.findRecruitingStudyByBookId(bookId, pageable)
                .stream()
                .map(studyConverter::studyToStudyInfo)
                .toList()
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

        return studyRepository.getStudyAppliers(studyId);
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


}
