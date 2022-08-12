package com.devcourse.checkmoi.domain.book.repository;

import static com.devcourse.checkmoi.domain.book.model.QBook.book;
import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.Search;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.global.model.OrderByNull;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Value("${order_by_field_list}")
    private String SQL_FUNCTION_ORDER_BY_FIELD_LIST;

    @Override
    public List<Book> findBooksByLatestStudy(Pageable page) {
        return jpaQueryFactory.select(book)
            .from(book)
            .innerJoin(study)
            .on(book.id.eq(study.book.id))
            .groupBy(book.id)
            .orderBy(study.createdAt.max().desc())
            .limit(page.getPageSize())
            .fetch();
    }

    @Override
    public Page<BookInfo> findAllByCondition(Search search, Pageable pageable) {

        List<Long> bookIds = findAssociateBookIds(search);

        JPQLQuery<BookInfo> query = jpaQueryFactory.select(
                Projections.constructor(
                    BookInfo.class,
                    book.id,
                    book.title,
                    book.author,
                    book.publisher,
                    book.publishedAt.publishedAt,
                    book.isbn,
                    book.thumbnail,
                    book.description,
                    book.createdAt
                )
            )
            .from(book)
            .where(
                inBookId(bookIds)
            )
            .orderBy(
                orderByFieldList(bookIds)
            );

        List<BookInfo> books = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return new PageImpl<>(books, pageable, bookIds.size());
    }

    private OrderSpecifier<?> orderByFieldList(List<Long> bookIds) {
        int MIN_BOOK_IDS_SIZE = 1;
        if (bookIds.size() <= MIN_BOOK_IDS_SIZE) {
            return OrderByNull.DEFAULT;
        }
        return Expressions.stringTemplate(SQL_FUNCTION_ORDER_BY_FIELD_LIST +
            "({0}, {1})", book.id, bookIds).desc();
    }

    private List<Long> findAssociateBookIds(Search search) {
        return jpaQueryFactory.select(
                study.book.id
            )
            .from(study)
            .where(
                eqStudyId(search.studyId()),
                eqStudyStatus(search.studyStatus()),
                eqBookId(search.bookId())
            )
            .groupBy(study.book.id)
            .orderBy(
                sortByMostStudy(search.mostStudy()),
                sortByLatestStudy(search.latestStudy())
            )
            .fetch();
    }

    private BooleanExpression eqStudyStatus(String studyStatus) {
        if (studyStatus == null) {
            return null;
        }
        return study.status.eq(StudyStatus.nameOf(studyStatus));
    }

    private BooleanExpression eqStudyId(Long studyId) {
        if (studyId == null) {
            return null;
        }
        return study.id.eq(studyId);
    }

    private BooleanExpression eqBookId(Long bookId) {
        if (bookId == null) {
            return null;
        }
        return book.id.eq(bookId);
    }

    private BooleanExpression inBookId(List<Long> bookIds) {
        if (bookIds.isEmpty()) {
            return null;
        }
        return book.id.in(bookIds);
    }

    private OrderSpecifier<?> sortByLatestStudy(Boolean latest) {
        if (latest == null) {
            return OrderByNull.DEFAULT;
        }
        return Boolean.TRUE.equals(latest) ?
            study.createdAt.max().desc() :  // 최신순
            study.createdAt.max().asc();    // 과거순
    }

    private OrderSpecifier<?> sortByMostStudy(Boolean most) {
        if (most == null) {
            return OrderByNull.DEFAULT;
        }
        return Boolean.TRUE.equals(most) ?
            study.book.count().desc() :  // 인기순
            study.book.count().asc();    // 비 인기순
    }
}
