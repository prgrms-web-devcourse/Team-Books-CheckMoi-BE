package com.devcourse.checkmoi.domain.study.model;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.global.model.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class StudyMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StudyMemberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @Builder
    public StudyMember(Long id, StudyMemberStatus status, User user, Study study) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.study = study;
    }

    public void changeStatus(StudyMemberStatus status) {
        this.status = status;
    }

    public StudyMemberStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Study getStudy() {
        return study;
    }

    public boolean isJoined() {
        return this.status == StudyMemberStatus.ACCEPTED ||
            this.status == StudyMemberStatus.OWNED;
    }

    public boolean isOwner() {
        return this.status == StudyMemberStatus.OWNED;
    }
}
