package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomStudyMemberRepositoryImpl implements
    CustomStudyMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long participateUserInStudy(Long studyId, Long userId) {
        return jpaQueryFactory.select(studyMember.id)
            .from(studyMember)
            .where(
                studyMember.study.id.eq(studyId)
                    .and(studyMember.user.id.eq(userId)),
                studyMember.status.eq(StudyMemberStatus.ACCEPTED)
                    .or(studyMember.status.eq(StudyMemberStatus.OWNED))
            ).fetchOne();
    }
}
