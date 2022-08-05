package com.devcourse.checkmoi.domain.book.converter;

import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.model.Book;
import com.devcourse.checkmoi.domain.book.stub.NewDummyData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookConverterTest {

    private static final BookConverter bookconverter = new BookConverter();

    @Test
    @DisplayName("Book 엔티티를  책 목록의 단일 책으로 변환한다")
    void bookToCreate() {
        NewDummyData bookRelatedInfo = createDummyWithoutId();

        Book whaleBook = bookRelatedInfo.book();
        SimpleBook expectedSimple = bookRelatedInfo.simple();

        SimpleBook convertedToSimple = bookconverter.bookToSimple(whaleBook);

        Assertions.assertThat(convertedToSimple)
            .usingRecursiveComparison()
            .isEqualTo(expectedSimple);
    }

    @Test
    @DisplayName("책 생성 요청 객체를 Book 엔티티로 변환한다")
    void createToBook() {
        NewDummyData bookRelatedInfo = createDummyWithoutId();

        CreateBook createRequest = bookRelatedInfo.create();
        Book expectedBook = bookRelatedInfo.book();

        Book createdBook = bookconverter.CreateToBook(createRequest);

        Assertions.assertThat(createdBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Book 엔티티를 책 상세 객체로 변환한다")
    void bookToSpecification() {
        NewDummyData bookRelatedInfo = createDummyWithoutId();

        Book book = bookRelatedInfo.book();
        BookSpecification expectedSpecification = bookRelatedInfo.specification();

        BookSpecification specification = bookconverter.bookToSpecification(book);

        Assertions.assertThat(specification)
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