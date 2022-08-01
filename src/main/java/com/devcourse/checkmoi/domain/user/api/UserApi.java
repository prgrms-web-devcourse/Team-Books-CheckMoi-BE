package com.devcourse.checkmoi.domain.user.api;

import com.devcourse.checkmoi.domain.user.dto.UserRequest;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfoWithStudy;
import com.devcourse.checkmoi.domain.user.service.UserCommandService;
import com.devcourse.checkmoi.domain.user.service.UserQueryService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApi {

    private final UserQueryService userQueryService;

    private final UserCommandService userCommandService;

    @GetMapping("/users/{userId}")
    ResponseEntity<SuccessResponse<UserInfoWithStudy>> userPage(
        @PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication user
    ) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(userQueryService.findUserInfoWithStudy(user.id())));
    }

    @DeleteMapping("/users/{userId}")
    ResponseEntity<Void> deleteAccount(
        @PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication user
    ) {
        userCommandService.deleteUserAccount(userId, user.id());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}")
    ResponseEntity<SuccessResponse<Long>> editAccount(
        @PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication user,
        @RequestBody UserRequest.Edit request
    ) {
        userCommandService.editAccount(userId, user.id(), request);
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(userId));
    }

}
