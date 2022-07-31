package com.devcourse.checkmoi.domain.study.model;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.global.model.BaseEntity;
import java.time.LocalDate;
import java.util.StringJoiner;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String thumbnailUrl;

    private String description;

    @Formula("(select count(1) from study_member sm where sm.study_id = id)")
    private int currentParticipant;

    private Integer maxParticipant;

    @Enumerated(value = EnumType.STRING)
    private StudyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private LocalDate gatherStartDate;

    private LocalDate gatherEndDate;

    private LocalDate studyStartDate;

    private LocalDate studyEndDate;

    public Study(String name, String thumbnailUrl, String description, Integer maxParticipant,
        StudyStatus status, Book book, LocalDate gatherStartDate, LocalDate gatherEndDate,
        LocalDate studyStartDate, LocalDate studyEndDate) {
        this(null, name, thumbnailUrl, description, status, maxParticipant, book, gatherStartDate,
            gatherEndDate, studyStartDate, studyEndDate);
    }

    @Builder
    public Study(Long id, String name, String thumbnailUrl, String description, StudyStatus status,
        Integer maxParticipant, Book book, LocalDate gatherStartDate, LocalDate gatherEndDate,
        LocalDate studyStartDate, LocalDate studyEndDate) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.status = status;
        this.maxParticipant = maxParticipant;
        this.book = book;
        this.gatherStartDate = gatherStartDate;
        this.gatherEndDate = gatherEndDate;
        this.studyStartDate = studyStartDate;
        this.studyEndDate = studyEndDate;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMaxParticipant() {
        return maxParticipant;
    }

    public LocalDate getGatherStartDate() {
        return gatherStartDate;
    }

    public LocalDate getGatherEndDate() {
        return gatherEndDate;
    }

    public LocalDate getStudyStartDate() {
        return studyStartDate;
    }

    public LocalDate getStudyEndDate() {
        return studyEndDate;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public int getCurrentParticipant() {
        return currentParticipant;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Study.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("name='" + name + "'")
            .add("thumbnailUrl='" + thumbnailUrl + "'")
            .add("description='" + description + "'")
            .add("currentParticipant=" + currentParticipant)
            .add("maxParticipant=" + maxParticipant)
            .add("status=" + status)
            .add("book=" + book)
            .add("gatherStartDate=" + gatherStartDate)
            .add("gatherEndDate=" + gatherEndDate)
            .add("studyStartDate=" + studyStartDate)
            .add("studyEndDate=" + studyEndDate)
            .toString();
    }
}
