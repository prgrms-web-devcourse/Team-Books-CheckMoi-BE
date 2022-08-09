package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.model.StudyMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long>,
    CustomStudyMemberRepository {

    @Query("select sm from StudyMember sm where sm.user.id = :userId")
    Optional<StudyMember> findByUserAndStudy(Long userId, Long studyId);

    @Query("select sm from StudyMember sm join fetch Study where sm.user.id = :userId and sm.study.id = :studyId")
    Optional<StudyMember> findWithStudyByUserAndStudy(Long userId, Long studyId);
}
