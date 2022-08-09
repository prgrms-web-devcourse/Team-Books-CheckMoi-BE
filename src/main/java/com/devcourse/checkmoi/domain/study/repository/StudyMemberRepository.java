package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.model.StudyMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long>, CustomStudyMemberRepository {

    @Query("select sm from StudyMember sm where sm.user.id = :userId")
    Optional<StudyMember> findByUserId(Long userId);

}
