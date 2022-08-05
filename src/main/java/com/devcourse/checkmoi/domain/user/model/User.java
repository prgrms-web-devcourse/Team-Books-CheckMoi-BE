package com.devcourse.checkmoi.domain.user.model;

import com.devcourse.checkmoi.domain.user.model.vo.Email;
import com.devcourse.checkmoi.global.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    private String provider;

    @Column(length = 20, nullable = false)
    private String name;

    @Embedded
    private Email email;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    private float temperature = 36.5f;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private User(String oauthId, String provider, String name, Email email,
        String profileImgUrl, float temperature, UserRole userRole) {
        this(null, oauthId, provider, name, email, profileImgUrl, temperature, userRole);
    }

    @Builder
    public User(Long id, String oauthId, String provider, String name,
        Email email, String profileImgUrl, float temperature,
        UserRole userRole) {
        this.id = id;
        this.oauthId = oauthId;
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.temperature = temperature;
        this.userRole = userRole;
    }

    public void editName(String name) {
        this.name = name;
    }

    public void editProfileImage(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }


    public Long getId() {
        return id;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public float getTemperature() {
        return temperature;
    }
}
