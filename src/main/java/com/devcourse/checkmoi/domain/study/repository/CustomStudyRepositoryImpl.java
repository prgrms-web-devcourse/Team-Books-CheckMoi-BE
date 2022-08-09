package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Search;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetail;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomStudyRepositoryImpl implements CustomStudyRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // TODO: Total Page
    @Override
    public List<StudyInfo> findAllByCondition(Long userId, Search search, Pageable pageable) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    StudyInfo.class,
                    study.id,
                    study.name,
                    study.thumbnailUrl,
                    study.description,
                    study.status,
                    study.currentParticipant, study.maxParticipant,
                    study.gatherStartDate, study.gatherEndDate,
                    study.studyStartDate, study.studyEndDate
                )
            )
            .from(study)
            .innerJoin(studyMember)
            .on(study.id.eq(studyMember.study.id))
            .where(
                eqUserId(search.userId()),
                eqStudyId(search.studyId()),
                eqBookId(search.bookId()),
                eqStudyMemberStatus(search.memberStatus()),
                eqStudyStatus(search.studyStatus())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Long findStudyOwner(Long studyId) {
        return jpaQueryFactory.select(studyMember.user.id)
            .from(studyMember)
            .where(
                study.id.eq(studyId),
                studyMember.status.eq(StudyMemberStatus.OWNED)
            )
            .fetchOne();
    }

    @Override
    public List<Study> findRecruitingStudyByBookId(Long bookId, Pageable pageable) {
        return jpaQueryFactory.select(study)
            .from(study)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .where(
                study.book.id.eq(bookId),
                study.status.eq(StudyStatus.RECRUITING)
            )
            .fetch();
    }

    @Override
    public StudyDetailWithMembers getStudyDetailWithMembers(Long studyId) {
        StudyDetail studyDetail = getStudyDetailInfo(studyId);

        List<StudyUserInfo> members =
            getStudyMembers(studyId, ACCEPTED, StudyMemberStatus.OWNED);

        return StudyDetailWithMembers.builder()
            .study(studyDetail.study())
            .book(studyDetail.book())
            .members(members)
            .build();
    }

    @Override
    public StudyAppliers getStudyApplicants(Long studyId) {
        List<StudyUserInfo> appliers = getStudyMembers(studyId, StudyMemberStatus.PENDING, null);

        return StudyAppliers.builder()
            .appliers(appliers)
            .build();
    }

    @Override
    public void updateAllApplicantsAsDenied(Long studyId) {
        jpaQueryFactory.update(studyMember)
            .where(studyMember.study.id.eq(studyId),
                studyMember.status.eq(StudyMemberStatus.PENDING))
            .set(studyMember.status, StudyMemberStatus.DENIED)
            .execute();
    }

    @Override
    public Studies getParticipationStudies(Long userId) {
        return new Studies(
            jpaQueryFactory.select(
                    Projections.constructor(
                        StudyInfo.class,
                        study.id, study.name, study.thumbnailUrl, study.description, study.status,
                        study.currentParticipant, study.maxParticipant,
                        study.gatherStartDate, study.gatherEndDate,
                        study.studyStartDate, study.studyEndDate
                    )
                )
                .from(study)
                .innerJoin(studyMember)
                .on(studyMember.study.id.eq(study.id))
                .where(
                    studyMember.user.id.eq(userId),
                    studyMember.status.eq(ACCEPTED)
                        .or(studyMember.status.eq(OWNED)),
                    study.status.eq(StudyStatus.IN_PROGRESS)
                        .or(study.status.eq(StudyStatus.RECRUITING))
                )
                .fetch()
        );
    }

    @Override
    public Studies getFinishedStudies(Long userId) {
        return new Studies(
            jpaQueryFactory.select(
                    Projections.constructor(
                        StudyInfo.class,
                        study.id, study.name, study.thumbnailUrl, study.description, study.status,
                        study.currentParticipant, study.maxParticipant, study.gatherStartDate,
                        study.gatherEndDate, study.studyStartDate, study.studyEndDate
                    )
                )
                .from(study)
                .innerJoin(studyMember)
                .on(studyMember.study.id.eq(study.id))
                .where(
                    studyMember.user.id.eq(userId),
                    studyMember.status.eq(ACCEPTED)
                        .or(studyMember.status.eq(OWNED)),
                    study.status.eq(StudyStatus.FINISHED)
                )
                .fetch()
        );
    }

    @Override
    public Studies getOwnedStudies(Long userId) {
        return new Studies(
            jpaQueryFactory.select(
                    Projections.constructor(
                        StudyInfo.class,
                        study.id, study.name, study.thumbnailUrl, study.description, study.status,
                        study.currentParticipant, study.maxParticipant, study.gatherStartDate,
                        study.gatherEndDate, study.studyStartDate, study.studyEndDate
                    )
                )
                .from(study)
                .innerJoin(studyMember)
                .on(studyMember.study.id.eq(study.id))
                .where(
                    studyMember.user.id.eq(userId),
                    studyMember.status.eq(OWNED)
                )
                .fetch()
        );
    }


    private StudyDetail getStudyDetailInfo(Long studyId) {
        return jpaQueryFactory.select(
                Projections.constructor(StudyDetail.class,
                    Projections.constructor(StudyInfo.class,
                        study.id,
                        study.name,
                        study.thumbnailUrl,
                        study.description,
                        study.status,
                        study.currentParticipant, study.maxParticipant,
                        study.gatherStartDate, study.gatherEndDate,
                        study.studyStartDate, study.studyEndDate
                    ),
                    Projections.constructor(StudyBookInfo.class,
                        study.book.id,
                        study.book.title,
                        study.book.thumbnail,
                        study.book.author,
                        study.book.publisher,
                        study.book.publishedAt.publishedAt,
                        study.book.isbn,
                        study.book.description,
                        study.book.createdAt
                    )
                ))
            .from(study)
            .innerJoin(study.book)
            .where(study.id.eq(studyId))
            .fetchOne();
    }

    private List<StudyUserInfo> getStudyMembers(
        Long studyId, StudyMemberStatus requiredStatus, StudyMemberStatus optionalStatus) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    StudyUserInfo.class,
                    studyMember.user.id,
                    studyMember.user.name,
                    studyMember.user.email.value.as("email"),
                    studyMember.user.profileImgUrl,
                    studyMember.user.temperature
                )
            )
            .from(studyMember)
            .innerJoin(studyMember.user)
            .where(
                eqStudyId(studyId),
                eqStudyMemberStatus(requiredStatus)
                    .or(eqStudyMemberStatus(optionalStatus)))
            .orderBy(studyMember.createdAt.asc())
            .fetch();
    }

    private BooleanExpression eqStudyMemberStatus(StudyMemberStatus status) {
        if (status == null) {
            return null;
        }
        return studyMember.status.eq(status);
    }

    private BooleanExpression eqStudyMemberStatus(String status) {
        if (status == null) {
            return null;
        }
        return studyMember.status.eq(StudyMemberStatus.valueOf(status));
    }

    private BooleanExpression eqStudyId(Long studyId) {
        if (studyId == null) {
            return null;
        }
        return studyMember.study.id.eq(studyId);
    }

    private BooleanExpression eqUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return studyMember.user.id.eq(userId);
    }

    private BooleanExpression eqStudyStatus(String status) {
        if (status == null) {
            return null;
        }
        return study.status.eq(StudyStatus.valueOf(status));
    }

    private BooleanExpression eqBookId(Long bookId) {
        if (bookId == null) {
            return null;
        }
        return study.book.id.eq(bookId);
    }
}
