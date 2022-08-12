package com.devcourse.checkmoi.domain.post.repository;

import static com.devcourse.checkmoi.domain.post.model.QPost.post;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.Posts;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Posts findAllByCondition(Long userId,
        Search search,
        Pageable pageable) {

        JPQLQuery<PostInfo> query = jpaQueryFactory.select(
                Projections.constructor(
                    PostInfo.class,
                    post.id,
                    post.title,
                    post.content,
                    post.category,
                    post.study.id,
                    post.writer.id,
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
                eqStudyId(search.studyId()),
                eqCategory(search.category())
            );

        long totalPage = query.fetchCount() / pageable.getPageSize();

        List<PostInfo> postInfos = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(
                getOrderSpecifier(pageable.getSort())
                    .toArray(OrderSpecifier[]::new))
            .fetch();

        return new PostResponse.Posts(totalPage, postInfos);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        return sort.stream()
            .map(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<Post> postPathBuilder = new PathBuilder<>(Post.class, "post");

                return new OrderSpecifier(direction, postPathBuilder.get(order.getProperty()));
            }).toList();
    }

    private BooleanExpression eqStudyId(Long studyId) {
        if (studyId == null) {
            return null;
        }
        return post.study.id.eq(studyId);
    }

    private BooleanExpression eqCategory(String category) {
        if (category == null) {
            return null;
        }
        return post.category.eq(PostCategory.valueOf(category));
    }

}
