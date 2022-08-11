package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.dto.ExpiredStudies;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Long findStudyOwner(Long studyId);

    Page<StudyInfo> findRecruitingStudyByBookId(Long bookId, Pageable pageable);

    StudyDetailWithMembers getStudyDetailWithMembers(Long studyId);

    StudyAppliers getStudyApplicants(Long studyId);

    void updateAllApplicantsAsDenied(Long studyId);

    Studies getParticipationStudies(Long userId);

    Studies getFinishedStudies(Long userId);

    Studies getOwnedStudies(Long userId);

    Page<StudyInfo> findAllByCondition(Long userId, Search search, Pageable pageable);

    ExpiredStudies getAllToBeProcessed(LocalDate current, StudyStatus toStatus);

    void updateStudyStatus(Long studyId, StudyStatus studyStatus);
}
