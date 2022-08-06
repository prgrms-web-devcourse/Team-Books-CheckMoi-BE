package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.MyStudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.model.Study;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Long findStudyOwner(Long studyId);

    List<Study> findRecruitingStudyByBookId(Long bookId, Pageable pageable);

    StudyDetailWithMembers getStudyInfoWithMembers(Long studyId);

    StudyAppliers getStudyAppliers(Long studyId);

    void updateAllAppliersAsDenied(Long studyId);

    List<MyStudyInfo> getMyStudies(Long userId);
}
