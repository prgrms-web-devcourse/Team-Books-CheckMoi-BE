package com.devcourse.checkmoi.domain.study.repository.study;

import com.devcourse.checkmoi.domain.study.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyRepository extends JpaRepository<Study, Long>, CustomStudyRepository {

}
