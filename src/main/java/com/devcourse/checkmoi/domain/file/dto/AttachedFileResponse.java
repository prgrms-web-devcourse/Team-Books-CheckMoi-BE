package com.devcourse.checkmoi.domain.file.dto;


import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse.Upload;
import java.util.List;

public sealed interface AttachedFileResponse permits Upload {

    record Upload(
        List<String> urls
    ) implements AttachedFileResponse {

    }
}
