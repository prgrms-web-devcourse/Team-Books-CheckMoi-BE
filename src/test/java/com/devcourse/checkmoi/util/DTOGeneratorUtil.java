package com.devcourse.checkmoi.util;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBook;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeStudyWithId;
import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeUserWithId;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.model.Post;
import com.devcourse.checkmoi.domain.post.model.PostCategory;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyInfo;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class DTOGeneratorUtil {


    // book
    public static BookInfo makeBookInfo(Book book) {
        return BookInfo.builder()
            .id(book.getId())
            .description(book.getDescription())
            .createdAt(book.getCreatedAt())
            .image(book.getThumbnail())
            .isbn(book.getIsbn())
            .author(book.getAuthor())
            .title(book.getTitle())
            .pubdate(book.getPublishedAt().getPublishedAt())
            .publisher(book.getPublisher())
            .build();
    }

    // study
    public static StudyInfo makeStudyInfo(Study study) {
        return StudyInfo.builder()
            .id(study.getId())
            .name(study.getName())
            .thumbnail(study.getThumbnailUrl())
            .description(study.getDescription())
            .status(study.getStatus())
            .currentParticipant(1)
            .maxParticipant(study.getMaxParticipant())
            .gatherStartDate(study.getGatherStartDate())
            .gatherEndDate(study.getGatherEndDate())
            .studyStartDate(study.getStudyStartDate())
            .studyEndDate(study.getStudyEndDate())
            .build();

    }

    // post
    public static PostInfo makePostInfo() {
        return PostInfo.builder()
            .id(1L)
            .title("제목")
            .content("본문")
            .category(PostCategory.GENERAL)
            .studyId(1L)
            .writer("user1")
            .writerImage("https://localhost/img.png")
            .commentCount(0)
            .build();
    }

    // user
    public static UserInfo makeUserInfo() {
        User user = makeUserWithId(1L);
        return UserInfo.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail().getValue())
            .image(user.getProfileImgUrl())
            .temperature(user.getTemperature())
            .build();
    }

    // studies
    public static List<Studies> makeMyStudies() {
        return List.of(
            new Studies(
                List.of(makeStudyInfo(makeStudyWithId(makeBook(), StudyStatus.IN_PROGRESS, 1L))), 1
            ),
            new Studies(
                List.of(makeStudyInfo(makeStudyWithId(makeBook(), StudyStatus.FINISHED, 2L))), 1
            ),
            new Studies(
                List.of(makeStudyInfo(makeStudyWithId(makeBook(), StudyStatus.RECRUITING, 3L))), 1
            )
        );
    }

    // comment
    public static CommentInfo makeCommentInfo(Long commentId, Long postId, Long userId) {
        return CommentInfo.builder()
            .id(commentId)
            .content("댓글 테스트")
            .postId(postId)
            .userId(userId)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public static CommentInfo makeCommentInfoWithId(User user, Post post, Long commentId) {
        return CommentInfo.builder().id(commentId).userId(user.getId()).userName(user.getName())
            .userImage(user.getProfileImgUrl()).postId(post.getId()).content("댓글 - " + UUID.randomUUID())
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }
}
