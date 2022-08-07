package com.devcourse.checkmoi.domain.comment.repository;

import static com.devcourse.checkmoi.domain.comment.model.QComment.comment;
import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentInfo> findAllByCondition(Long userId, Search request) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    CommentInfo.class,
                    comment.id,
                    comment.user.id,
                    comment.post.id,
                    comment.content,
                    comment.createdAt,
                    comment.updatedAt
                )
            )
            .from(comment)
            .where(
                eqUserId(userId),
                eqPostId(request.postId())
            )
            .fetch();
    }

    private BooleanExpression eqUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return comment.user.id.eq(userId);
    }

    private BooleanExpression eqPostId(Long postId) {
        if (postId == null) {
            return null;
        }
        return comment.post.id.eq(postId);
    }


}
