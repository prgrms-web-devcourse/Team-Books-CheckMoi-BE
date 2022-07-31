package com.devcourse.checkmoi.domain.study.repository.study;

import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomStudyRepositoryImpl implements
    CustomStudyRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long findStudyOwner(Long studyId) {
        return jpaQueryFactory.select(studyMember.user.id)
            .from(study)
            .innerJoin(studyMember)
            .on(study.id.eq(studyMember.study.id))
            .where(
                study.id.eq(studyId),
                studyMember.status.eq(StudyMemberStatus.OWNED)
            )
            .fetchOne();
    }

    @Override
    public Page<Study> findRecruitingStudyByBookId(Long bookId, Pageable pageable) {
        return new PageImpl<>(
            jpaQueryFactory.select(study)
                .from(study)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(
                    study.book.id.eq(bookId),
                    study.status.eq(StudyStatus.RECRUTING)
                )
                .fetch()
        );
    }

}
