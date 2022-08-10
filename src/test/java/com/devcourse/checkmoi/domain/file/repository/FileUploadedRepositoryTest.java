package com.devcourse.checkmoi.domain.file.repository;

import com.devcourse.checkmoi.domain.file.model.FileUploaded;
import com.devcourse.checkmoi.domain.user.repository.UserRepository;
import com.devcourse.checkmoi.template.RepositoryTest;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FileUploadedRepositoryTest extends RepositoryTest {

    @Autowired
    FileUploadedRepository fileUploadedRepository;

    @Autowired
    UserRepository userRepository;

    private FileUploaded file;

    @BeforeEach
    void setUp() {
        file = FileUploaded.builder()
            .extension("jpeg")
            .originalFileName("/abc/foo.jpeg")
            .userId(1L)
            .build();

        fileUploadedRepository.save(file);
    }

    @Test
    @DisplayName("S url 을 통해 업로드 되었던 이미지에 대한 정보를 찾아온다")
    void findFile() {
        Optional<FileUploaded> optionalFile = fileUploadedRepository.findByUrl(file.getUrl());

        Assertions.assertThat(optionalFile.isPresent())
            .isTrue();
    }
}