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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public Study(String name, String thumbnailUrl, String description, Integer maxParticipant,
        Book book, LocalDate gatherStartDate, LocalDate gatherEndDate,
        LocalDate studyStartDate, LocalDate studyEndDate) {
        this(null, name, thumbnailUrl, description, maxParticipant, book, gatherStartDate,
            gatherEndDate, studyStartDate, studyEndDate);
    }

    @Builder
    public Study(Long id, String name, String thumbnailUrl, String description,
        Integer maxParticipant, Book book, LocalDate gatherStartDate, LocalDate gatherEndDate,
        LocalDate studyStartDate, LocalDate studyEndDate) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.maxParticipant = maxParticipant;
        this.book = book;
        this.gatherStartDate = gatherStartDate;
        this.gatherEndDate = gatherEndDate;
        this.studyStartDate = studyStartDate;
        this.studyEndDate = studyEndDate;
    }

    public void editName(String name) {
        this.name = name;
    }

    public void editThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void editDescription(String description) {
        this.description = description;
    }
}
