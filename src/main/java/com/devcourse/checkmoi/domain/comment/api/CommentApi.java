package com.devcourse.checkmoi.domain.comment.api;

import com.devcourse.checkmoi.domain.comment.dto.CommentRequest.Search;
import com.devcourse.checkmoi.domain.comment.dto.CommentResponse.CommentInfo;
import com.devcourse.checkmoi.domain.comment.service.CommentCommandService;
import com.devcourse.checkmoi.domain.comment.service.CommentQueryService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentApi {

    private final CommentQueryService commentQueryService;

    private final CommentCommandService commentCommandService;

    @GetMapping("/comments")
    public ResponseEntity<SuccessResponse<List<CommentInfo>>> findAllComments(
        @AuthenticationPrincipal JwtAuthentication user,
        Search request
    ) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(commentQueryService.findAllComments(user.id(), request)));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @AuthenticationPrincipal JwtAuthentication user,
        @PathVariable Long commentId
    ) {
        commentCommandService.deleteById(user.id(), commentId);
        return ResponseEntity.noContent().build();
    }

}
