package com.devcourse.checkmoi.domain.user.api;

import com.devcourse.checkmoi.domain.user.dto.response.MyUserInfoResponse;
import com.devcourse.checkmoi.domain.user.service.UserService;
import com.devcourse.checkmoi.global.model.SuccessResponse;
import com.devcourse.checkmoi.global.security.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    ResponseEntity<SuccessResponse<MyUserInfoResponse>> myPage(
        @PathVariable Long userId, @AuthenticationPrincipal JwtAuthentication user) {
        return ResponseEntity.ok()
            .body(new SuccessResponse<>(userService.findUserInfo(user.id())));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAccount(@PathVariable Long userId, @AuthenticationPrincipal JwtAuthentication user) {
        userService.deleteUserAccount(user.id());
    }

}
