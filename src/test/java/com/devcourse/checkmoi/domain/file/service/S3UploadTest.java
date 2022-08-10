package com.devcourse.checkmoi.domain.file.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class S3UploadTest {

    @Autowired
    private S3Upload s3Upload;

    @Test
    @DisplayName("S S3에 실제 파일을 업로드 하고 삭제한다")
    void uploadAndDelete() throws IOException {
        String filePath = "src/test/java/com/devcourse/checkmoi/domain/file/service/img.png";
        String key = "abc.png";

        File file = new File(filePath);

        Assertions.assertThat(file.exists())
            .isTrue();

        String uploadedUrl = s3Upload.upload(
            Files.readAllBytes(file.toPath()),
            key,
            "image/png",
            null
        );

        Assertions.assertThat(uploadedUrl)
            .isNotBlank();

        s3Upload.delete(uploadedUrl);
    }
}