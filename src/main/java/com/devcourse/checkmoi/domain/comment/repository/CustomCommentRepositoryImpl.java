package com.devcourse.checkmoi.domain.comment.repository;

import static com.devcourse.checkmoi.domain.comment.model.QComment.comment;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentInfo> findAllByCondition(Search request, Pageable pageable) {
        JPQLQuery<CommentInfo> query = jpaQueryFactory.select(
                Projections.constructor(
                    CommentInfo.class,
                    comment.id,
                    comment.user.id,
                    comment.user.name,
                    comment.user.profileImgUrl,
                    comment.post.id,
                    comment.content,
                    comment.createdAt,
                    comment.updatedAt
                )
            )
            .from(comment)
            .where(
                eqPostId(request.postId())
            )
            .orderBy(comment.createdAt.asc());
        long totalCount = query.fetchCount();
        List<CommentInfo> comments = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return new PageImpl<>(comments, pageable, totalCount);
    }

    private BooleanExpression eqPostId(Long postId) {
        if (postId == null) {
            return null;
        }
        return comment.post.id.eq(postId);
    }


}
