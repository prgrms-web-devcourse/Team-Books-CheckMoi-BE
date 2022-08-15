package com.devcourse.checkmoi.domain.book.converter;

import static com.devcourse.checkmoi.util.EntityGeneratorUtil.makeBookWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookInfo;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.stub.NewDummyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookConverterTest {

    private static final BookConverter bookconverter = new BookConverter();

    @Test
    @DisplayName("Book 엔티티를  책 목록의 단일 책으로 변환한다")
    void bookToCreate() {
        NewDummyData bookRelatedInfo = createDummyWithoutId();

        Book whaleBook = bookRelatedInfo.book();
        BookInfo expectedSimple = bookRelatedInfo.simple();

        BookInfo convertedToSimple = bookconverter.bookToInfo(whaleBook);

        assertThat(convertedToSimple)
            .usingRecursiveComparison()
            .isEqualTo(expectedSimple);
    }

    @Test
    @DisplayName("Book 엔티티를  책 목록의 단일 책으로 변환한다2")
    void bookToCreate2() {
        Book book = makeBookWithId(1L);
        BookInfo bookInfo = bookconverter.bookToInfo(book);

        assertAll(
            () -> assertThat(bookInfo.id()).isEqualTo(book.getId()),
            () -> assertThat(bookInfo.isbn()).isEqualTo(book.getIsbn()),
            () -> assertThat(bookInfo.title()).isEqualTo(book.getTitle()),
            () -> assertThat(bookInfo.author()).isEqualTo(book.getAuthor()),
            () -> assertThat(
                bookInfo.pubdate()).isEqualTo(book.getPublishedAt().getPublishedAt()),
            () -> assertThat(bookInfo.description()).isEqualTo(book.getDescription())
        );
    }

    @Test
    @DisplayName("책 생성 요청 객체를 Book 엔티티로 변환한다")
    void createToBook() {
        NewDummyData bookRelatedInfo = createDummyWithoutId();

        CreateBook createRequest = bookRelatedInfo.create();
        Book expectedBook = bookRelatedInfo.book();

        Book createdBook = bookconverter.CreateToBook(createRequest);

        assertThat(createdBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Book 엔티티를 책 상세 객체로 변환한다")
    void bookToSpecification() {
        NewDummyData bookRelatedInfo = createDummyWithoutId();

        Book book = bookRelatedInfo.book();
        BookInfo expectedSpecification = bookRelatedInfo.simple();

        BookInfo specification = bookconverter.bookToInfo(book);

        assertThat(specification)
            .usingRecursiveComparison()
            .isEqualTo(expectedSpecification);
    }

    private NewDummyData createDummyWithoutId() {
        return new NewDummyData("큰그림",
            "대왕고래",
            "abc/foo.png",
            null,
            "UNDEFINED",
            "대왕고래와 아기고래가 함께 살았어요",
            "1231231231231",
            "Hanbit",
            "20021010");
    }

}
