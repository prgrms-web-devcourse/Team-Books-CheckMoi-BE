package com.devcourse.checkmoi.domain.study.repository.study;

import com.devcourse.checkmoi.domain.study.model.Study;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomStudyRepository {

    Long findStudyOwner(Long studyId);

    Page<Study> findRecruitingStudyByBookId(Long bookId, Pageable pageable);
}
