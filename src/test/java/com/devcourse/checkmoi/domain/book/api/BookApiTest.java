package com.devcourse.checkmoi.domain.book.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.book.dto.BookRequest.CreateBook;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.BookSpecification;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.LatestAllBooks;
import com.devcourse.checkmoi.domain.book.dto.BookResponse.SimpleBook;
import com.devcourse.checkmoi.domain.book.service.BookCommandService;
import com.devcourse.checkmoi.domain.book.service.BookQueryService;
import com.devcourse.checkmoi.domain.book.stub.PersistedDummyData;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

class BookApiTest extends IntegrationTest {

    @MockBean
    BookCommandService bookCommandService;

    @MockBean
    BookQueryService bookQueryService;

    private PersistedDummyData createDummyBigWhale() {
        return new PersistedDummyData("큰그림", "대왕고래", "abc/foo.png", 1L, "대왕고래와 아기고래가 함께 살았어요",
            "1231231231231", "Hanbit", "20021010");
    }

    private PersistedDummyData createDummyWhaleTwo() {
        return new PersistedDummyData("큰그림", "향고래", "abc/foo.png", 2L, "향고래를 닮았네요",
            "1231231231232", "Hanbit", "20021010");
    }

    @Nested
    @DisplayName("책 등록 테스트")
    public class UpdateBookTest {

        private final PersistedDummyData bookInfo = createDummyBigWhale();

        @Test
        @WithMockUser
        @DisplayName("S 로그인 사용자는 책을 등록할 수 있다")
        void save() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            CreateBook createRequest = bookInfo.create();
            SimpleBook simpleBook = bookInfo.simple();

            given(bookCommandService.save(createRequest)).willReturn(simpleBook);

            MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/books")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                    .content(toJson(createRequest))).andExpect(status().isOk()).andDo(documentation())
                .andReturn();

            SuccessResponse<Long> result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

            Assertions.assertThat(result.data()).isEqualTo(bookInfo.bookId());
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("book-create",
                ResourceSnippetParameters.builder().tag("Book API").summary("책 등록")
                    .description("책 등록에 사용되는 API 입니다.")
                    .requestSchema(Schema.schema("책 생성 요청"))
                    .responseSchema(Schema.schema("책 생성 응답")),
                tokenRequestHeader(),
                requestFields(fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
                    fieldWithPath("image").type(JsonFieldType.STRING).description("책 이미지"),
                    fieldWithPath("author").type(JsonFieldType.STRING).description("책 저자"),
                    fieldWithPath("publisher").type(JsonFieldType.STRING).description("책 출판사"),
                    fieldWithPath("pubdate").type(JsonFieldType.STRING).description("책 발행날자"),
                    fieldWithPath("isbn").type(JsonFieldType.STRING).description("책 isbn13"),
                    fieldWithPath("description").type(JsonFieldType.STRING)
                        .description("책에 대한 설명")), responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("책 아이디")));
        }
    }

    @Nested
    @DisplayName("메인화면에 책 목록 불러오기 테스트")
    public class TopBooksTest {

        private LatestAllBooks books;

        @BeforeEach
        void setUp() {
            PersistedDummyData bigWhale = createDummyBigWhale();
            PersistedDummyData whaleTwo = createDummyWhaleTwo();

            books = new LatestAllBooks(
                List.of(bigWhale.simple(), whaleTwo.simple()));
        }

        @Test
        @DisplayName("페이지 네이션 없이 최대 8개의 책 목록을 가져온다")
        void topBooks() throws Exception {
            given(bookQueryService.getAllTop(any()))
                .willReturn(books);

            MvcResult mvcResult = mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andDo(documentation())
                .andReturn();
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("book-top",
                ResourceSnippetParameters.builder().tag("Book API").summary("메인 페이지 책 목록 받아오기")
                    .description("메인페이지에 책 목록을 가져오는 API 입니다")
                    .responseSchema(Schema.schema("책 목록 응답")),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.books").type(JsonFieldType.ARRAY).description("책 목록"),
                    fieldWithPath("data.books[].id").type(JsonFieldType.NUMBER).description("책 ID"),
                    fieldWithPath("data.books[].title").type(JsonFieldType.STRING)
                        .description("책 제목"),
                    fieldWithPath("data.books[].image").type(JsonFieldType.STRING)
                        .description("책 이미지"),
                    fieldWithPath("data.books[].author").type(JsonFieldType.STRING)
                        .description("책 저자"),
                    fieldWithPath("data.books[].publisher").type(JsonFieldType.STRING)
                        .description("책 출판사"),
                    fieldWithPath("data.books[].isbn").type(JsonFieldType.STRING)
                        .description("책 ISBN"),
                    fieldWithPath("data.books[].description").type(JsonFieldType.STRING)
                        .description("책 설명"),
                    fieldWithPath("data.books[].createAt").type(JsonFieldType.STRING)
                        .description("책 등록날자"),
                    fieldWithPath("data.books[].pubDate").type(JsonFieldType.STRING)
                        .description("책 발행날자")
                ));
        }
    }

    @Nested
    @DisplayName("책 조회 테스트")
    public class GetTest {

        @Test
        @DisplayName("id 를 통해 책을 조회한다")
        void getByIdSuccess() throws Exception {
            PersistedDummyData bigWhaleBook = createDummyBigWhale();

            BookSpecification specification = bigWhaleBook.specification();

            given(bookQueryService.getById(anyLong())).willReturn(specification);

            MvcResult mvcResult = mockMvc.perform(get("/api/books/{bookId}", bigWhaleBook.bookId()))
                .andExpect(status().isOk())
                .andDo(documentation())
                .andReturn();
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("book-getById",
                ResourceSnippetParameters.builder().tag("Book API").summary("책 id 에 해당하는 책 가져오기")
                    .description("책 id 에 해당하는 책을 가져오는 API 입니다")
                    .responseSchema(Schema.schema("책 상세정보 응답")),
                pathParameters(
                    parameterWithName("bookId").description("책 id")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("책 id"),
                    fieldWithPath("data.image").type(JsonFieldType.STRING).description("책 이미지"),
                    fieldWithPath("data.author").type(JsonFieldType.STRING).description("책 저자"),
                    fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("책 출판사"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("책 제목"),
                    fieldWithPath("data.pubDate").type(JsonFieldType.STRING).description("책 발행날자"),
                    fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("책 isbn13"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING)
                        .description("책에 대한 설명"),
                    fieldWithPath("data.createAt").type(JsonFieldType.STRING)
                        .description("책 등록 날자"))
            );
        }
    }


}