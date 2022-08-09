package com.devcourse.checkmoi.domain.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApi {

    @GetMapping("/")
    public ResponseEntity<String> healthy() {
        return ResponseEntity.ok("Check Moi");
    }

    @GetMapping("/login")
    public ResponseEntity<String> token(@RequestParam("token") String token) {
        return ResponseEntity.ok(token);
    }
}
