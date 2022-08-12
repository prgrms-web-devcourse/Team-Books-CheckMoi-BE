package com.devcourse.checkmoi.domain.study.repository;

import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.study.model.StudyStatus.FINISHED;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyBookInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetail;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyMemberInfo;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyUserInfo;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

@Component
public class CustomRepositoryUtil {

    /******* Constructor  *******/

    public static ConstructorExpression<StudyDetail> studyDetailConstructor() {
        return Projections.constructor(StudyDetail.class,
            studyInfoConstructor(),
            studyBookInfoConstructor()
        );
    }

    public static ConstructorExpression<StudyBookInfo> studyBookInfoConstructor() {
        return Projections.constructor(
            StudyBookInfo.class,
            study.book.id,
            study.book.title,
            study.book.thumbnail,
            study.book.author,
            study.book.publisher,
            study.book.publishedAt.publishedAt,
            study.book.isbn,
            study.book.description,
            study.book.createdAt
        );
    }

    public static ConstructorExpression<StudyMemberInfo> StudyMemberInfoConstructor() {
        return Projections.constructor(
            StudyMemberInfo.class,
            studyMember.id,
            studyUserInfoConstructor()
        );
    }

    public static ConstructorExpression<StudyUserInfo> studyUserInfoConstructor() {
        return Projections.constructor(
            StudyUserInfo.class,
            studyMember.user.id,
            studyMember.user.name,
            studyMember.user.email.value.as("email"),
            studyMember.user.profileImgUrl,
            studyMember.user.temperature
        );
    }

    public static ConstructorExpression<StudyInfo> studyInfoConstructor() {
        return Projections.constructor(
            StudyInfo.class,
            study.id, study.name, study.thumbnailUrl, study.description, study.status,
            study.currentParticipant, study.maxParticipant, study.gatherStartDate,
            study.gatherEndDate, study.studyStartDate, study.studyEndDate
        );
    }


    /******* Expression *******/
    public static BooleanExpression isStudyParticipant(boolean participant) {
        return participant ?
            studyMember.status.in(OWNED, ACCEPTED) :
            studyMember.status.notIn(OWNED, ACCEPTED);
    }

    public static BooleanExpression isFinished(boolean finish) {
        return finish ?
            study.status.eq(FINISHED) :
            study.status.notIn(FINISHED);
    }

    public static BooleanExpression eqStudyMemberStatus(StudyMemberStatus... statuses) {
        if (statuses.length == 0) {
            return null;
        }
        return studyMember.status.in(statuses);
    }

    public static BooleanExpression eqStudyMemberStatus(String status) {
        if (status == null) {
            return null;
        }
        return studyMember.status.eq(StudyMemberStatus.valueOf(status.toUpperCase()));
    }

    public static BooleanExpression eqStudyId(Long studyId) {
        if (studyId == null) {
            return null;
        }
        return studyMember.study.id.eq(studyId);
    }

    public static BooleanExpression eqUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return studyMember.user.id.eq(userId);
    }

    public static BooleanExpression eqStudyStatus(String status) {
        if (status == null) {
            return null;
        }
        return study.status.eq(StudyStatus.nameOf(status));
    }

    public static BooleanExpression eqBookId(Long bookId) {
        if (bookId == null) {
            return null;
        }
        return study.book.id.eq(bookId);
    }

    public static BooleanExpression isMember(Boolean condition) {
        if (condition == null) {
            return null;
        }

        if (condition.equals(Boolean.FALSE)) {
            return studyMember.status.notIn(OWNED, ACCEPTED).or(study.status.eq(FINISHED));
        }

        return studyMember.status.in(OWNED, ACCEPTED).and(study.status.notIn(FINISHED));
    }
}
