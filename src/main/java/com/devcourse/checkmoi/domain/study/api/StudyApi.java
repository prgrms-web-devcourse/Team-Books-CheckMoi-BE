package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.service.study.StudyCommandService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyApi {

    private final StudyCommandService studyCommandService;

    @PostMapping("/studies")
    public ResponseEntity<SuccessResponse<Long>> createStudy(
        @RequestBody Create request) {
        Long studyId = studyCommandService.createStudy(request);
        return ResponseEntity
            .created(generatedUri(studyId))
            .body(new SuccessResponse<>(studyId));
    }

    @PutMapping("/studies/{studyId}")
    public ResponseEntity<SuccessResponse<Long>> editStudyInfo(
        @PathVariable Long studyId, @RequestBody Edit request) {
        return ResponseEntity.ok(
            new SuccessResponse<>(studyCommandService.editStudyInfo(studyId, request)));
    }
}
