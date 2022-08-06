package com.devcourse.checkmoi.domain.post.model;

import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.global.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 6000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Study study;

    @ManyToOne(optional = false)
    private User user;

    public Post(PostCategory category, String title, String content, Study study, User user) {
        this(null, category, title, content, study, user);
    }

    @Builder
    public Post(Long id, PostCategory category, String title, String content,
        Study study, User user) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.study = study;
        this.user = user;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public PostCategory getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Study getStudy() {
        return study;
    }

    public User getUser() {
        return user;
    }
}
