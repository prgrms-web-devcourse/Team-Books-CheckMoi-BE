package com.devcourse.checkmoi.domain.comment.model;

import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.global.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private Post post;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Builder
    public Comment(Long id, Post post, User user, String content) {
        this.id = id;
        this.post = post;
        this.user = user;
        this.content = content;
    }

    public Comment(Post post, User user, String content) {
        this(null, post, user, content);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void updateComment(String comment) {
        this.content = comment;
    }

    public Post getPost() {
        return post;
    }

    public String getContent() {
        return content;
    }
}
