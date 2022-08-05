package com.devcourse.checkmoi.domain.file.repository;

import com.devcourse.checkmoi.domain.file.model.FileUploaded;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadedRepository extends JpaRepository<FileUploaded, Long> {

    Optional<FileUploaded> findByUrl(String url);
}
