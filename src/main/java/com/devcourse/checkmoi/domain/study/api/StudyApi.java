package com.devcourse.checkmoi.domain.study.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Audit;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Create;
import com.devcourse.checkmoi.domain.study.dto.StudyRequest.Edit;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.Studies;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyAppliers;
import com.devcourse.checkmoi.domain.study.dto.StudyResponse.StudyDetailWithMembers;
import com.devcourse.checkmoi.domain.study.service.StudyCommandService;
import com.devcourse.checkmoi.domain.study.service.StudyQueryService;
import com.devcourse.checkmoi.global.model.PageRequest;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
public class StudyApi {

    private final StudyCommandService studyCommandService;

    private final StudyQueryService studyQueryService;

    @GetMapping("/{studyId}")
    public ResponseEntity<SuccessResponse<StudyDetailWithMembers>> getDetailInfo(
        @PathVariable Long studyId
    ) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(studyQueryService.getStudyInfoWithMembers(studyId)));
    }


    @PostMapping
    public ResponseEntity<SuccessResponse<Long>> createStudy(
        @RequestBody Create request,
        @AuthenticationPrincipal JwtAuthentication user) {
        Long studyId = studyCommandService.createStudy(request, user.id());
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

    @PutMapping("/{studyId}/members/{memberId}")
    public ResponseEntity<Void> auditStudyParticipation(
        @PathVariable Long studyId,
        @PathVariable Long memberId,
        @AuthenticationPrincipal JwtAuthentication user,
        @RequestBody Audit request
    ) {
        studyCommandService.auditStudyParticipation(studyId, memberId, user.id(), request);
        return ResponseEntity
            .noContent()
            .build();
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Studies>> getStudies(
        @RequestParam Long bookId,
        PageRequest pageRequest
    ) {
        Pageable pageable = pageRequest.of();
        Studies response = studyQueryService.getStudies(bookId, pageable);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PutMapping("/{studyId}/members")
    public ResponseEntity<SuccessResponse<Long>> requestStudyJoin(@PathVariable Long studyId,
        @AuthenticationPrincipal JwtAuthentication user) {
        Long studyMemberId = studyCommandService.requestStudyJoin(studyId, user.id());
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(studyMemberId));
    }

    @GetMapping("/{studyId}/members")
    public ResponseEntity<SuccessResponse<StudyAppliers>> getStudyAppliers(
        @PathVariable Long studyId,
        @AuthenticationPrincipal JwtAuthentication user) {

        return ResponseEntity.ok()
            .body(new SuccessResponse<>(studyQueryService.getStudyAppliers(user.id(), studyId)));
    }
}
