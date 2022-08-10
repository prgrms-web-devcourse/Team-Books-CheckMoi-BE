package com.devcourse.checkmoi.domain.post.repository;

import static com.devcourse.checkmoi.domain.post.model.QPost.post;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostInfo> findAllByCondition(Long userId, Search request) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    PostInfo.class,
                    post.id,
                    post.title,
                    post.content,
                    post.category,
                    post.study.id,
                    post.writer.name,
                    post.writer.profileImgUrl,
                    post.commentCount,
                    post.createdAt,
                    post.updatedAt
                )
            )
            .from(post)
            .innerJoin(post.writer)
            .where(
                eqStudyId(request.studyId())
            )
            .fetch();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        if (studyId == null) {
            return null;
        }
        return post.study.id.eq(studyId);
    }

}
