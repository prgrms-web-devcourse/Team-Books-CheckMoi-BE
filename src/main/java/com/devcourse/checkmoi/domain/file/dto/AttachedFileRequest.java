package com.devcourse.checkmoi.domain.file.dto;

import com.devcourse.checkmoi.domain.file.dto.AttachedFileRequest.Upload;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public sealed interface AttachedFileRequest permits Upload {

    record Upload(
        List<MultipartFile> files
    ) implements AttachedFileRequest {

    }
}
