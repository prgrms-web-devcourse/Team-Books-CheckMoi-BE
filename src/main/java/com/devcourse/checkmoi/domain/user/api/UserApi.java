package com.devcourse.checkmoi.domain.user.api;

import com.devcourse.checkmoi.domain.user.dto.UserRequest;
import com.devcourse.checkmoi.domain.user.dto.UserResponse.UserInfo;
import com.devcourse.checkmoi.domain.user.service.UserService;
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

    private final UserService userService;

    @GetMapping("/users/{userId}")
    ResponseEntity<SuccessResponse<UserInfo>> userPage(
        @PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication user
    ) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(userService.findUserInfo(user.id())));
    }

    @DeleteMapping("/users/{userId}")
    ResponseEntity<Void> deleteAccount(
        @PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication user
    ) {
        userService.deleteUserAccount(userId, user.id());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{userId}")
    ResponseEntity<SuccessResponse<Long>> editAccount(
        @PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication user,
        @RequestBody UserRequest.Edit request
    ) {
        userService.editAccount(userId, user.id(), request);
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(userId));
    }

}
