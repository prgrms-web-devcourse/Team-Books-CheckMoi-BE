package com.devcourse.checkmoi.domain.study.repository.study;

import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

}
