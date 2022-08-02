package com.devcourse.checkmoi.domain.file.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse.Upload;
import com.devcourse.checkmoi.domain.file.service.FileServiceImpl;
import com.devcourse.checkmoi.domain.token.dto.TokenResponse.TokenWithUserInfo;
import com.devcourse.checkmoi.template.IntegrationTest;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ImageApiTest extends IntegrationTest {

    @MockBean
    FileServiceImpl fileUploadService;

    @Nested
    @DisplayName("이미지 업로드 테스트")
    public class UploadImageTest {

        private String notBlankPath = "fileName.jpeg";

        private String notBlankFileName = "fileName.jpeg";

        private byte[] notEmptyContents = new byte[]{1};

        @Test
        @DisplayName("S 이미지 업로드시 url 리스트를 반환한다")
        void updateImageSuccess() throws Exception {
            TokenWithUserInfo givenUser = getTokenWithUserInfo();

            AttachedFileResponse.Upload uploadResponse = new Upload(
                List.of("https://ap-xx/abcdefe.jpeg"));

            given(fileUploadService.upload(any(), any()))
                .willReturn(uploadResponse);

            ResultActions resultActions = mockMvc.perform(multipart("/api/image")
                .file(new MockMultipartFile("files", notBlankPath,
                    "image/jpeg", notEmptyContents))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + givenUser.accessToken())
                .contentType(MediaType.MULTIPART_FORM_DATA).characterEncoding("utf-8"));

            resultActions.andExpect(status().isOk())
                .andDo(documentation())
                .andReturn();
        }

        private RestDocumentationResultHandler documentation() {
            return MockMvcRestDocumentationWrapper.document("image-upload",
                ResourceSnippetParameters.builder()
                    .tag("Image API")
                    .summary("이미지 업로드")
                    .description("이미지 업로드에 사용되는 API입니다.")
                    .requestSchema(Schema.schema("파일 첨부"))
                    .responseSchema(Schema.schema("업로드된 파일 url")),
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParts(partWithName("files").description("업로드 할 파일")),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                    fieldWithPath("data.urls[]").type(JsonFieldType.ARRAY)
                        .description("업로드 파일 url 리스트")
                )
            );
        }
    }


}