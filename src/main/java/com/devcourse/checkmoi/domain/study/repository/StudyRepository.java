package com.devcourse.checkmoi.domain.study.repository;

import com.devcourse.checkmoi.domain.study.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyRepository extends JpaRepository<Study, Long>, CustomStudyRepository {

//    @Transactional
//    @Query(nativeQuery = true,
//        value = "update study_member sm join study s "
//            + "set sm.status = 'DENIED' "
//            + "where s.gather_end_date < current_date and sm.status = 'PENDING'")
//    void updateExpiredApplicantsAsDenied(LocalDate curDate);
}
