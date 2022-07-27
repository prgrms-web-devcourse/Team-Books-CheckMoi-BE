package com.devcourse.checkmoi.domain.user.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.devcourse.checkmoi.domain.user.exception.UserInvalidValueException;
import com.devcourse.checkmoi.global.model.BaseEntity;
import com.devcourse.checkmoi.global.vo.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    private static final int NAME_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    private String provider;

    @Column(length = NAME_MAX_LENGTH, nullable = false)
    private String name;

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private User(String oauthId, String provider, String name, Email email,
        String profileImgUrl, UserRole userRole){
        this(null, oauthId, provider, name, email, profileImgUrl, userRole);
    }
    @Builder
    public User(Long id, String oauthId, String provider, String name, Email email,
        String profileImgUrl, UserRole userRole) {
        this.oauthId = oauthId;
        this.name = name;
        this.provider = provider;
        this.profileImgUrl = profileImgUrl;
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

}
