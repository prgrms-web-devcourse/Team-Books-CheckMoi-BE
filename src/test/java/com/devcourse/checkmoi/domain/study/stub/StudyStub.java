package com.devcourse.checkmoi.domain.study.stub;

import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.stub.BookStub;
import com.devcourse.checkmoi.domain.study.model.Study;
import com.devcourse.checkmoi.domain.study.model.StudyStatus;
import java.time.LocalDate;
import java.util.List;

public class StudyStub {
    private static final List<Book> book = BookStub.StubBook();

    public static List<String> javaRecrutingStudyNameStub() {
        return List.of(
            "자바 스터디 1",
            "자바 스터디 3"
        );
    }

    public static List<Study> javaRecrutingStudyStub() {
        return List.of(
            Study.builder()
                .id(1L)
                .name("자바 스터디 1")
                .thumbnailUrl("https://example.com/java.png")
                .description("자바 스터디 1번입니다.")
                .maxParticipant(3)
                .status(StudyStatus.RECRUTING)
                .book(book.get(0))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build(),
            Study.builder()
                .id(3L)
                .name("자바 스터디 3")
                .thumbnailUrl("https://example.com/java3.png")
                .description("자바 스터디 3번입니다.")
                .maxParticipant(3)
                .status(StudyStatus.RECRUTING)
                .book(book.get(0))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build()
        );
    }
    public static List<Study> studiesStub() {
        return List.of(
            Study.builder()
                .id(1L)
                .name("자바 스터디 1")
                .thumbnailUrl("https://example.com/java.png")
                .description("자바 스터디 1번입니다.")
                .maxParticipant(3)
                .status(StudyStatus.RECRUTING)
                .book(book.get(0))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build(),
            Study.builder()
                .id(2L)
                .name("자바 스터디 2")
                .thumbnailUrl("https://example.com/java2.png")
                .description("자바 스터디 2번입니다.")
                .maxParticipant(7)
                .status(StudyStatus.FINISHED)
                .book(book.get(0))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build(),
            Study.builder()
                .id(3L)
                .name("자바 스터디 3")
                .thumbnailUrl("https://example.com/java3.png")
                .description("자바 스터디 3번입니다.")
                .maxParticipant(3)
                .status(StudyStatus.RECRUTING)
                .book(book.get(0))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build(),
            Study.builder()
                .id(4L)
                .name("자바스크립트 스터디 1")
                .thumbnailUrl("https://example.com/java.png")
                .description("자바스크립트 스터디 1번입니다.")
                .maxParticipant(3)
                .status(StudyStatus.RECRUTING)
                .book(book.get(1))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build(),
            Study.builder()
                .id(5L)
                .name("자바스크립트 스터디 2")
                .thumbnailUrl("https://example.com/java.png")
                .description("자바스크립트 스터디 2번입니다.")
                .maxParticipant(11)
                .status(StudyStatus.IN_PROGRESS)
                .book(book.get(1))
                .gatherStartDate(LocalDate.now())
                .gatherEndDate(LocalDate.now())
                .studyStartDate(LocalDate.now())
                .studyEndDate(LocalDate.now())
                .build()
        );
    }

}
