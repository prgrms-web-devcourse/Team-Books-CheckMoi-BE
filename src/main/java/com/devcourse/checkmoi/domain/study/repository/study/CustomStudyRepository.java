package com.devcourse.checkmoi.domain.study.repository.study;

import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.model.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Long findStudyOwner(Long studyId);

    Page<Study> findRecruitingStudyByBookId(Long bookId, Pageable pageable);

    StudyDetailWithMembers getStudyInfoWithMembers(Long studyId);
}
