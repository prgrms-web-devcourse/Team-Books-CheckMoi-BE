package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface StudyRepository extends JpaRepository<Study, Long>, CustomStudyRepository {

    @Query("select sm.user.id from StudyMember sm "
        + "where sm.study.id = :studyId and sm.status = 'OWNED'")
    Long findStudyOwner(Long studyId);
}
