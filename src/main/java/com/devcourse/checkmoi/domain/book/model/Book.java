package com.devcourse.checkmoi.domain.book.model;

import com.devcourse.checkmoi.global.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 50)
    private String author;

    @Column(length = 50)
    private String publisher;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false, length = 20, unique = true)
    private String isbn;

    @Column(length = 1000)
    private String thumbnail;

    @Embedded
    private PublishedDate publishedAt;

    @Column(length = 10000)
    private String description;

    @Builder
    public Book(Long id, String title, String author, String publisher, String isbn,
        String thumbnail, PublishedDate publishedAt, String description) {
        this(id, title, author, publisher, Category.UNDEFINED, isbn, thumbnail, publishedAt,
            description);
    }

    private Book(Long id, String title, String author, String publisher,
        Category category, String isbn, String thumbnail,
        PublishedDate publishedAt, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
        this.publishedAt = publishedAt;
        this.description = description;
    }

    public PublishedDate getPublishedAt() {
        return publishedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public Category getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }
}
