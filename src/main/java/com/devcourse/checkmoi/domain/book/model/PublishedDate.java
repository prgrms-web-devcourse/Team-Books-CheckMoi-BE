package com.devcourse.checkmoi.domain.book.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublishedDate {

    @Transient
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Column
    private LocalDate publishedAt; // TODO : 외부에서 받은 문자열을 LocalDate 로 변환하는 것이기 때문에, 예외상황에 기본값을 부여하는 로직이 필요할 것 같다 ( 어쩔수 없이 예외를 발생시키면 안되는 부분이라 )

    private PublishedDate(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public PublishedDate(String publishedAt) {
        this(LocalDate.parse(publishedAt, dateFormat));
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }
}
