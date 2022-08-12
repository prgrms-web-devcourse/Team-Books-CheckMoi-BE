package com.devcourse.checkmoi.domain.user.repository;

import static com.devcourse.checkmoi.domain.study.model.QStudyMember.studyMember;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.ACCEPTED;
import static com.devcourse.checkmoi.domain.study.model.StudyMemberStatus.OWNED;
import static com.devcourse.checkmoi.domain.user.model.QUser.user;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfoWithStudy;
import com.devcourse.checkmoi.domain.user.dto.UserStudyResponse.StudyInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserInfoWithStudy findUserInfoWithStudy(Long userId) {

        List<StudyInfo> studyInfos = getUserStudiesInfo(userId, 5);
        UserInfo user = getUserInfo(userId);

        return UserInfoWithStudy.builder()
            .id(user.id())
            .name(user.name())
            .email(user.email())
            .temperature(user.temperature())
            .image(user.image())
            .studies(studyInfos)
            .build();
    }

    private List<StudyInfo> getUserStudiesInfo(Long userId, int limit) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    StudyInfo.class,
                    studyMember.study.id, studyMember.study.name, studyMember.study.thumbnailUrl
                )
            ).from(studyMember)
            .innerJoin(studyMember.study)
            .innerJoin(studyMember.user)
            .where(
                studyMember.status.in(OWNED, ACCEPTED)
            )
            .limit(5)
            .on(studyMember.user.id.eq(userId))
            .where(
                studyMember.status.in(OWNED, ACCEPTED)
            )
            .limit(limit)
            .fetch();
    }

    private UserInfo getUserInfo(Long userId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    UserInfo.class,
                    user.id, user.name, user.email.value.as("email"),
                    user.temperature, user.profileImgUrl
                )
            )
            .from(user)
            .where(user.id.eq(userId))
            .fetchOne();
    }
}
