package com.meongmungi.controller;

import com.meongmungi.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/login/failure")
    public ResponseEntity<Map<String, String>> loginFailure() {
        return ResponseEntity.badRequest().body(Map.of("error", "로그인 실패"));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(Map.of(
            "user", Map.of(
                "email", customOAuth2User.getUser().getEmail(),
                "nickname", customOAuth2User.getUser().getNickname(),
                "provider", customOAuth2User.getUser().getProvider()
            )
        ));
    }
} 