package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.PENDING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.RECRUITING_FINISHED;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.StudyMemberInfoConstructor;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.eqBookId;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.eqStudyId;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.eqStudyMemberStatus;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.eqStudyStatus;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.eqUserId;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.isFinished;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.isMember;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.isStudyParticipant;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.studyDetailConstructor;
import static com.devcourse.checkmoi.domain.study.repository.CustomRepositoryUtil.studyInfoConstructor;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetail;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMemberInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMembers;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.study.service.dto.ExpiredStudies;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Collections;
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
    public Page<StudyInfo> findAllByCondition(Long userId, Search search, Pageable pageable) {
        JPQLQuery<StudyInfo> query = jpaQueryFactory
            .select(studyInfoConstructor())
            .from(study)
            .innerJoin(studyMember)
            .on(study.id.eq(studyMember.study.id))
            .where(
                eqUserId(search.userId()),
                eqStudyId(search.studyId()),
                eqBookId(search.bookId()),
                isMember(search.isMember()),
                eqStudyMemberStatus(search.memberStatus()),
                eqStudyStatus(search.studyStatus())
            )
            .groupBy(study.id);

        List<StudyInfo> studies = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(studies, pageable, query.fetchCount());
    }

    @Override
    public Page<StudyInfo> findRecruitingStudyByBookId(Long bookId, Pageable pageable) {
        JPQLQuery<StudyInfo> query = jpaQueryFactory
            .select(studyInfoConstructor())
            .from(study)
            .where(
                eqBookId(bookId),
                eqStudyStatus(RECRUITING.getMappingCode())
            );

        List<StudyInfo> studies = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(studies, pageable, query.fetchCount());
    }

    @Override
    public StudyDetailWithMembers getStudyDetailWithMembers(Long studyId) {
        StudyDetail studyDetail = getStudyDetailInfo(studyId);
        List<StudyMemberInfo> members = getStudyMembers(studyId, ACCEPTED, OWNED);
        return StudyDetailWithMembers.builder()
            .study(studyDetail.study())
            .book(studyDetail.book())
            .members(members)
            .build();
    }

    @Override
    public StudyMembers getStudyApplicants(Long studyId) {
        List<StudyMemberInfo> members = getStudyMembers(studyId, PENDING);

        return StudyMembers.builder()
            .members(members)
            .build();
    }

    @Override
    public Studies getParticipationStudies(Long userId) {
        return new Studies(
            jpaQueryFactory
                .select(studyInfoConstructor())
                .from(study)
                .innerJoin(studyMember)
                .on(studyMember.study.id.eq(study.id))
                .where(
                    eqUserId(userId),
                    isStudyParticipant(true),
                    isFinished(false)
                )
                .fetch(), 0
        );
    }

    @Override
    public Studies getFinishedStudies(Long userId) {
        return new Studies(
            jpaQueryFactory
                .select(studyInfoConstructor())
                .from(study)
                .innerJoin(studyMember)
                .on(studyMember.study.id.eq(study.id))
                .where(
                    eqUserId(userId),
                    isStudyParticipant(true),
                    isFinished(true)
                )
                .fetch(), 0
        );
    }

    @Override
    public Studies getOwnedStudies(Long userId) {
        return new Studies(
            jpaQueryFactory
                .select(studyInfoConstructor())
                .from(study)
                .innerJoin(studyMember)
                .on(studyMember.study.id.eq(study.id))
                .where(
                    eqUserId(userId),
                    eqStudyMemberStatus(OWNED.name())
                )
                .fetch(), 0
        );
    }

    @Override
    public ExpiredStudies getAllTobeProgressed(LocalDate current, StudyStatus toStatus) {
        return switch (toStatus) {
            case IN_PROGRESS -> findAllToBeProgressed(current);
            default -> new ExpiredStudies(Collections.emptyList());
        };
    }

    /***** update ********/
    @Override
    public void updateStudyStatus(Long studyId, StudyStatus studyStatus) {
        jpaQueryFactory.update(study)
            .set(study.status, studyStatus)
            .where(study.id.eq(studyId))
            .execute();
    }

    @Override
    public void updateAllApplicantsAsDenied(Long studyId) {
        jpaQueryFactory
            .update(studyMember)
            .where(
                studyMember.study.id.eq(studyId),
                eqStudyMemberStatus(PENDING)
            )
            .set(studyMember.status, StudyMemberStatus.DENIED)
            .execute();
    }

    private ExpiredStudies findAllToBeProgressed(LocalDate current) {
        return new ExpiredStudies(
            jpaQueryFactory
                .select(study.id)
                .from(study)
                .where(
                    study.studyStartDate.between(null, current),
                    study.status.eq(RECRUITING)
                        .or(study.status.eq(RECRUITING_FINISHED))
                ).fetch()
        );
    }

    /****** helper *******/

    private StudyDetail getStudyDetailInfo(Long studyId) {
        return jpaQueryFactory
            .select(studyDetailConstructor())
            .from(study)
            .innerJoin(study.book)
            .where(study.id.eq(studyId))
            .fetchOne();
    }

    private List<StudyMemberInfo> getStudyMembers(
        Long studyId, StudyMemberStatus... memberStatuses
    ) {
        return jpaQueryFactory
            .select(StudyMemberInfoConstructor())
            .from(studyMember)
            .innerJoin(studyMember.user)
            .where(
                eqStudyId(studyId),
                eqStudyMemberStatus(memberStatuses)
            )
            .orderBy(studyMember.createdAt.asc())
            .fetch();
    }


}
