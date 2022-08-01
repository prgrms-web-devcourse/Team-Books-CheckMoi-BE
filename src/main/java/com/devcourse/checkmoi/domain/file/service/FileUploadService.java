package com.devcourse.checkmoi.domain.file.service;

import com.devcourse.checkmoi.domain.file.dto.AttachedFileRequest;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse;

public interface FileUploadService {

    AttachedFileResponse.Upload upload(AttachedFileRequest.Upload request, Long userId);

    void delete(String url);
}
