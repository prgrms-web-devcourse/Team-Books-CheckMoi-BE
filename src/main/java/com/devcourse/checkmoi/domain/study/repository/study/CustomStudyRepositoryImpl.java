package com.devcourse.checkmoi.domain.study.repository.study;

import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomStudyRepositoryImpl implements CustomStudyRepository {

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

    @Override
    public StudyDetailWithMembers getStudyInfoWithMembers(Long studyId) {
        StudyDetailInfo studyInfo = getStudyInfo(studyId);
        List<UserInfo> memberInfo = getStudyMembers(studyId, StudyMemberStatus.ACCEPTED,
            StudyMemberStatus.OWNED);

        return StudyDetailWithMembers.builder()
            .study(studyInfo)
            .members(memberInfo)
            .build();
    }

    @Override
    public StudyAppliers getStudyAppliers(
        Long studyId) {
        List<UserInfo> appliers = getStudyMembers(studyId, StudyMemberStatus.PENDING, null);

        return StudyAppliers.builder()
            .appliers(appliers)
            .build();
    }

    private StudyDetailInfo getStudyInfo(Long studyId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    StudyDetailInfo.class,
                    study.id, study.name, study.status.stringValue(),
                    study.thumbnailUrl, study.description,
                    study.currentParticipant, study.maxParticipant,
                    study.gatherStartDate, study.gatherEndDate,
                    study.studyStartDate, study.studyEndDate,
                    Projections.constructor(
                        StudyBookInfo.class,
                        study.book.id, study.book.title,
                        study.book.author, study.book.publisher, study.book.thumbnail
                    )
                ))
            .from(study)
            .innerJoin(study.book)
            .where(study.id.eq(studyId))
            .fetchOne();
    }

    private List<UserInfo> getStudyMembers(Long studyId, StudyMemberStatus requiredStatus,
        StudyMemberStatus optionalStatus) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    UserInfo.class,
                    studyMember.user.id,
                    studyMember.user.name,
                    studyMember.user.email.value.as("email"),
                    studyMember.user.temperature,
                    studyMember.user.profileImgUrl
                )
            )
            .from(studyMember)
            .innerJoin(studyMember.user)
            .where(
                eqStudyId(studyId),
                eqStatus(requiredStatus)
                    .or(eqStatus(optionalStatus)))
            .orderBy(studyMember.createdAt.asc())
            .fetch();
    }

    private BooleanExpression eqStatus(StudyMemberStatus status) {
        if (status == null) {
            return null;
        }
        return studyMember.status.eq(status);
    }

    private BooleanExpression eqStudyId(Long studyId) {
        if (studyId == null) {
            return null;
        }
        return studyMember.study.id.eq(studyId);
    }
}
