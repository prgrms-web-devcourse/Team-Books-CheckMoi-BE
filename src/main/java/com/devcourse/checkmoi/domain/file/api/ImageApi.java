package com.devcourse.checkmoi.domain.file.api;

import com.devcourse.checkmoi.domain.file.dto.AttachedFileRequest;
import com.devcourse.checkmoi.domain.file.dto.AttachedFileResponse;
import com.devcourse.checkmoi.domain.file.service.FileService;
import com.devcourse.checkmoi.domain.file.service.FileServiceImpl;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImageApi {

    private final FileService uploadService;

    public ImageApi(FileServiceImpl uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/image")
    public ResponseEntity<SuccessResponse<AttachedFileResponse.Upload>> uploadImage(
        @AuthenticationPrincipal JwtAuthentication user,
        AttachedFileRequest.Upload uploadRequest
    ) {

        return ResponseEntity.ok(
            new SuccessResponse<>(uploadService.upload(uploadRequest, user.id())
            ));
    }
}
