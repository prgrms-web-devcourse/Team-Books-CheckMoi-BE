package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Long findStudyOwner(Long studyId);

    List<Study> findRecruitingStudyByBookId(Long bookId, Pageable pageable);

    StudyDetailWithMembers getStudyDetailWithMembers(Long studyId);

    StudyAppliers getStudyApplicants(Long studyId);

    void updateAllApplicantsAsDenied(Long studyId);

    Studies getParticipationStudies(Long userId);

    Studies getFinishedStudies(Long userId);

    Studies getOwnedStudies(Long userId);

    List<StudyInfo> findAllByCondition(Long userId, Search search, Pageable pageable);
}
