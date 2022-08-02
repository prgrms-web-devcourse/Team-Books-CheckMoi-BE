package com.devcourse.checkmoi.domain.file.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileRequest.Upload;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse;
import com.devcourse.checkmoi.domain.file.model.FileUploaded;
import com.devcourse.checkmoi.domain.file.repository.FileUploadedRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @InjectMocks
    FileServiceImpl fileUploadService;

    @Mock
    S3Upload s3Upload;

    @Mock
    FileUploadedRepository repository;

    private String notBlankPath = "fileName.jpeg";

    private String notBlankFileName = "fileName.jpeg";

    private byte[] notEmptyContents = new byte[]{1};

    @Test
    @DisplayName("S MultipartFile 형태의 파일들이 주어지면 해당 파일들이 업로드된 url 리스트를 반환한다")
    void upload() {
        Long userId = 1L;
        MultipartFile multipartFile1 = new MockMultipartFile(notBlankPath, notBlankFileName,
            "image/jpeg", notEmptyContents);
        MultipartFile multipartFile2 = new MockMultipartFile(notBlankPath, notBlankFileName,
            "image/jpeg", notEmptyContents);

        Upload uploadRequest = new Upload(List.of(multipartFile1, multipartFile2));

        given(s3Upload.upload(any(), any(), any(), any()))
            .willReturn("fileName.jpeg");
        given(repository.save(any()))
            .willReturn(FileUploaded.builder().build());

        AttachedFileResponse.Upload urls
            = fileUploadService.upload(uploadRequest, userId);

        Assertions.assertThat(urls.urls().size())
            .isEqualTo(2);
    }
}