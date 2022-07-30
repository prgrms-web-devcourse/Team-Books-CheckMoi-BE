package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.service.study.StudyCommandService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyApi {

    private final StudyCommandService studyCommandService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Long>> createStudy(
        @RequestBody Create request) {
        Long studyId = studyCommandService.createStudy(request);
        return ResponseEntity
            .created(generatedUri(studyId))
            .body(new SuccessResponse<>(studyId));
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<SuccessResponse<Long>> editStudyInfo(
        @PathVariable Long studyId, @RequestBody Edit request,
        @AuthenticationPrincipal JwtAuthentication user) {
        return ResponseEntity.ok(
            new SuccessResponse<>(studyCommandService.editStudyInfo(studyId, user.id(), request)));
    }
}
