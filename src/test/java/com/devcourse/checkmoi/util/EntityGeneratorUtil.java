package com.devcourse.checkmoi.util;

import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.UserRole;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import java.time.LocalDate;
import java.util.UUID;

public abstract class EntityGeneratorUtil {

    public static StudyMember makeStudyMember(Study study, User user, StudyMemberStatus status) {
        return StudyMember.builder()
            .status(status)
            .user(user)
            .study(study)
            .build();
    }

    public static Study makeStudy(Book book) {
        String name = UUID.randomUUID().toString().substring(20);
        return Study.builder()
            .name("스터디-" + name)
            .thumbnailUrl("https://example.com/java.png")
            .description("자바 스터디")
            .maxParticipant(3)
            .status(StudyStatus.RECRUITING)
            .book(book)
            .gatherStartDate(LocalDate.now())
            .gatherEndDate(LocalDate.now())
            .studyStartDate(LocalDate.now())
            .studyEndDate(LocalDate.now())
            .build();
    }

    public static User makeUser() {
        String name = UUID.randomUUID().toString().substring(26);
        return User.builder()
            .oauthId("ASDASDQWDAASDZFWEF1")
            .provider("KAKAO")
            .name(name)
            .temperature(36.5f)
            .email(new Email(name + "@test.com"))
            .profileImgUrl("https://example.com/java.png")
            .userRole(UserRole.LOGIN)
            .build();
    }

    public static User makeSecondNonStudyMemberUser() {
        String name = UUID.randomUUID().toString().substring(26);

        return User.builder()
            .oauthId("ASDASDQWDAASDZFWEF4")
            .provider("KAKAO")
            .name(name)
            .temperature(36.5f)
            .email(new Email(name + "@test.com"))
            .profileImgUrl("https://example.com/java.png")
            .userRole(UserRole.LOGIN)
            .build();
    }

    public static User makeNonStudyMemberUser() {
        String name = UUID.randomUUID().toString().substring(26);

        return User.builder()
            .oauthId("ASDASDQWDAASDZFWEF2")
            .provider("KAKAO")
            .name(name)
            .temperature(36.5f)
            .email(new Email(name + "@test.com"))
            .profileImgUrl("https://example.com/java.png")
            .userRole(UserRole.LOGIN)
            .build();
    }

    public static User makeStudyMemberUser() {
        String name = UUID.randomUUID().toString().substring(26);
        return User.builder()
            .oauthId("ASDASDQWDAASDZFWEF3")
            .provider("KAKAO")
            .name(name)
            .temperature(36.5f)
            .email(new Email(name + "@test.com"))
            .profileImgUrl("https://example.com/java.png")
            .userRole(UserRole.LOGIN)
            .build();
    }

    public static Book makeBook() {
        String title = UUID.randomUUID().toString().substring(10);
        String isbn = UUID.randomUUID().toString().substring(20);
        return Book.builder()
            .title(title)
            .description("자바책")
            .author("김자바")
            .publisher("자바출판")
            .isbn(isbn)
            .thumbnail("https://example.com/java.png")
            .publishedAt(new PublishedDate("20121111"))
            .build();
    }
}
