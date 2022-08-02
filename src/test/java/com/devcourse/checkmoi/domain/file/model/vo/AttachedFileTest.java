package com.devcourse.checkmoi.domain.file.model.vo;

import com.devcourse.checkmoi.domain.file.exception.NotAllowedFileException;
import com.devcourse.checkmoi.domain.file.model.vo.AttachedFile.FileType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class AttachedFileTest {

    private String paramName = "files";

    private String notBlankFileName = "fileName.jpeg";

    private byte[] notEmptyContents = new byte[]{1};

    @Nested
    @DisplayName("AttachedFile 생성 테스트")
    public class CreateTest {

        @Test
        @DisplayName("S 기대하는 컨텐트 타입을 가진 MultipartFile 로부터 AttachedFile 을 생성한다")
        void createAttachedFile() {
            FileType expectedMimeTye = FileType.IMAGE;
            MultipartFile multipartFile = new MockMultipartFile(paramName,
                notBlankFileName, "image/jpeg", notEmptyContents);

            AttachedFile file = AttachedFile.toAttachedFile(
                multipartFile, expectedMimeTye);

            Assertions.assertThat(file.getOriginalFileName())
                .isEqualTo(notBlankFileName);
        }

        @Test
        @DisplayName("F 기대하는 컨텐트 타입이 아닌 경우 AttachedFile 생성에 실패한다")
        void createAttachedFileFail() {
            FileType expectedMimeTye = FileType.IMAGE;

            MultipartFile multipartFile = new MockMultipartFile(paramName,
                notBlankFileName, "text/html", notEmptyContents);

            Assertions.assertThatThrownBy(
                    () -> AttachedFile.toAttachedFile(multipartFile, expectedMimeTye))
                .isInstanceOf(NotAllowedFileException.class);
        }
    }

    @Nested
    @DisplayName("경로 생성 테스트")
    public class CreatePathTest {

        private String contentType = "image/png";

        @Test
        @DisplayName("S 전달된 basePath 에 유니크한 문자열을 더한 경로를 생성한다")
        void createPath() {

            MultipartFile multipartFile1 = new MockMultipartFile(paramName, paramName,
                contentType, notEmptyContents);
            MultipartFile multipartFile2 = new MockMultipartFile(paramName, paramName,
                contentType, notEmptyContents);

            String basePath = "foo";

            String generatedPath1 = AttachedFile.toAttachedFile(multipartFile1, FileType.IMAGE)
                .randomName(basePath, null);

            String generatedPath2 = AttachedFile.toAttachedFile(multipartFile1, FileType.IMAGE)
                .randomName(basePath, null);

            Assertions.assertThat(generatedPath1)
                .isNotEqualTo(generatedPath2);
        }
    }
}