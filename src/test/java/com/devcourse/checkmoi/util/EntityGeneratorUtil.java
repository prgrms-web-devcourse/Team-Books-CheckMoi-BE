package com.devcourse.checkmoi.util;

import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.model.Book.BookBuilder;
import com.devcourse.checkmoi.domain.book.model.PublishedDate;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.Post.PostBuilder;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.Study.StudyBuilder;
import com.devcourse.checkmoi.domain.study.model.StudyMember;
import com.devcourse.checkmoi.domain.study.model.StudyMember.StudyMemberBuilder;
import com.devcourse.checkmoi.domain.study.model.StudyMemberStatus;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.model.User;
import com.devcourse.checkmoi.domain.user.model.User.UserBuilder;
import com.devcourse.checkmoi.domain.user.model.UserRole;
import com.devcourse.checkmoi.domain.user.model.vo.Email;
import java.time.LocalDate;
import java.util.UUID;

public abstract class EntityGeneratorUtil {

    // basic create entity
    public static StudyMember makeStudyMember(Study study, User user, StudyMemberStatus status) {
        return studyMemberBuilder(study, user, status).build();
    }

    public static Study makeStudy(Book book, StudyStatus status) {
        return studyBuilder(book, status).build();
    }

    public static User makeUser() {
        return userBuilder().build();
    }

    public static Book makeBook() {
        return bookBuilder().build();
    }

    public static Post makePost(PostCategory category, Study study, User user) {
        return postBuilder(category, study, user).build();
    }


    // with Id
    public static StudyMember makeStudyMemberWithId(
        Study study, User user, StudyMemberStatus status, Long id
    ) {
        return studyMemberBuilder(study, user, status).id(id).build();
    }

    public static Study makeStudyWithId(Book book, StudyStatus status, Long id) {
        return studyBuilder(book, status).id(id).build();
    }

    public static User makeUserWithId(Long id) {
        return userBuilder().id(id).build();
    }

    public static Book makeBookWithId(Long id) {
        return bookBuilder().id(id).build();
    }

    public static Post makePostWithId(PostCategory category, Study study, User user, Long id) {
        return postBuilder(category, study, user).id(id).build();
    }

    // builder
    private static UserBuilder userBuilder() {
        String name = UUID.randomUUID().toString().substring(26);
        return User.builder()
            .oauthId("ASDASDQWDAASDZFWEF1")
            .provider("KAKAO")
            .name(name)
            .temperature(36.5f)
            .email(new Email(name + "@test.com"))
            .profileImgUrl("https://example.com/java.png")
            .userRole(UserRole.LOGIN);
    }

    private static StudyMemberBuilder studyMemberBuilder(
        Study study, User user, StudyMemberStatus status
    ) {
        return StudyMember.builder()
            .status(status)
            .user(user)
            .study(study);
    }

    private static StudyBuilder studyBuilder(Book book, StudyStatus status) {
        String name = UUID.randomUUID().toString().substring(20);
        return Study.builder()
            .name("스터디-" + name)
            .thumbnailUrl("https://example.com/java.png")
            .description("자바 스터디")
            .maxParticipant(3)
            .status(status)
            .book(book)
            .gatherStartDate(LocalDate.now())
            .gatherEndDate(LocalDate.now())
            .studyStartDate(LocalDate.now())
            .studyEndDate(LocalDate.now());
    }

    private static PostBuilder postBuilder(PostCategory category, Study study, User user) {
        String title = UUID.randomUUID().toString().substring(10);

        return Post.builder()
            .title("제목-" + title)
            .content("본문-" + title)
            .category(category)
            .study(study) // TODO: 스터디 멤버로 변경?
            .writer(user);
    }

    private static BookBuilder bookBuilder() {
        String title = UUID.randomUUID().toString().substring(10);
        String isbn = UUID.randomUUID().toString().substring(20);

        return Book.builder()
            .title(title)
            .author("제비")
            .description("제비가 좋아")
            .publisher("Hanbit")
            .isbn(isbn)
            .thumbnail("https://example.com/abc/jebi.png")
            .publishedAt(new PublishedDate("20121111"));
    }

}
