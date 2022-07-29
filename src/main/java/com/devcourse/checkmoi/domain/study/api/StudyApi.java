package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.CreateStudy;
import com.devcourse.checkmoi.domain.study.service.study.StudyCommandService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudyApi {

    private final StudyCommandService studyCommandService;

    // C 스터디 개설 #5
    @PostMapping("/studies")
    public ResponseEntity<SuccessResponse<Long>> createStudy(
        @RequestBody CreateStudy request) {
        Long studyId = studyCommandService.createStudy(request);
        return ResponseEntity
            .created(generatedUri(studyId))
            .body(new SuccessResponse<>(studyId));
    }
}
