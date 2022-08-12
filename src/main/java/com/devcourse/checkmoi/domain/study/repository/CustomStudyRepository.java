package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMembers;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.dto.ExpiredStudies;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Page<StudyInfo> findRecruitingStudyByBookId(Long bookId, Pageable pageable);

    StudyDetailWithMembers getStudyDetailWithMembers(Long studyId);

    StudyMembers getStudyApplicants(Long studyId);


    Studies getParticipationStudies(Long userId);

    Studies getFinishedStudies(Long userId);

    Studies getOwnedStudies(Long userId);


    Page<StudyInfo> findAllByCondition(Search search, Pageable pageable);

    ExpiredStudies getAllTobeProgressed(LocalDate current, StudyStatus toStatus);

    void updateStudyStatus(Long studyId, StudyStatus studyStatus);

    void updateAllApplicantsAsDenied(Long studyId);
}
