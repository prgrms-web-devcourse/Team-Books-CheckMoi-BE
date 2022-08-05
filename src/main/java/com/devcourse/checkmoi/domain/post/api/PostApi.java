package com.devcourse.checkmoi.domain.post.api;

import static com.devcourse.checkmoi.global.util.ApiUtil.generatedUri;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Create;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Edit;
import com.devcourse.checkmoi.domain.post.dto.PostRequest.Search;
import com.devcourse.checkmoi.domain.post.dto.PostResponse.PostInfo;
import com.devcourse.checkmoi.domain.post.service.PostCommandService;
import com.devcourse.checkmoi.domain.post.service.PostQueryService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApi {

    private final PostCommandService postCommandService;

    private final PostQueryService postQueryService;

    // 공통 : 스터디에 해당하는 유저만 Post CR 권한이 있다
    // 공통 : 글을 작성한 본인만 UD 권한이 있다

    @GetMapping("/posts")
    ResponseEntity<SuccessResponse<List<PostInfo>>> findAllPosts(
        @AuthenticationPrincipal JwtAuthentication user,
        @RequestBody Search request
    ) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(postQueryService.findAllPosts(user.id(), request)));
    }

    @GetMapping("/posts/{postId}")
    ResponseEntity<SuccessResponse<PostInfo>> findPost(
        @AuthenticationPrincipal JwtAuthentication user,
        @PathVariable Long postId
    ) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(postQueryService.findByPostId(user.id(), postId)));
    }

    @PostMapping("/posts")
    ResponseEntity<SuccessResponse<Long>> createPost(
        @AuthenticationPrincipal JwtAuthentication user,
        @RequestBody Create request
    ) {
        Long postId = postCommandService.createPost(user.id(), request);
        return ResponseEntity
            .created(generatedUri(postId))
            .body(new SuccessResponse<>(postId));
    }

    @PutMapping("/posts/{postId}")
    ResponseEntity<SuccessResponse<Void>> editPost(
        @AuthenticationPrincipal JwtAuthentication user,
        Long postId,
        @RequestBody Edit request
    ) {
        postCommandService.editPost(user.id(), postId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}")
    ResponseEntity<SuccessResponse<Void>> deletePost(
        @AuthenticationPrincipal JwtAuthentication user,
        @PathVariable Long postId
    ) {
        postCommandService.deletePost(user.id(), postId);
        return ResponseEntity.noContent().build();
    }
}
