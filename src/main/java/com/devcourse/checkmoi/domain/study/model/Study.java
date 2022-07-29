package com.devcourse.checkmoi.domain.study.model;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.global.model.BaseEntity;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String thumbnailUrl;
    private String description;
    private Integer maxParticipant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private LocalDate gatherStartDate;
    private LocalDate gatherEndDate;

    private LocalDate studyStartDate;
    private LocalDate studyEndDate;

}
