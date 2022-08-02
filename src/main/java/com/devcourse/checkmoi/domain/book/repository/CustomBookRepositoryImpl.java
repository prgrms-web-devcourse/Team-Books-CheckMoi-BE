package com.devcourse.checkmoi.domain.book.repository;

import static com.devcourse.checkmoi.domain.book.model.QBook.book;
import static com.devcourse.checkmoi.domain.study.model.QStudy.study;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements
    CustomBookRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Book> findBooksByLatestStudy(Pageable page) {
        return jpaQueryFactory.select(book)
            .from(book)
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .innerJoin(study)
            .on(book.id.eq(study.book.id))
            .orderBy(study.createdAt.desc())
            .fetch();
    }
}
