package org.ll.ai_recommendation.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ll.ai_recommendation.domain.member.entity.Member;
import org.ll.ai_recommendation.domain.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class ApiV1MemberController {
    private final MemberService memberService;

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String apiKey) {
        Member member = memberService.findByApiKey(apiKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key"));

        String newAccessToken = memberService.genAccessToken(member);

        // 응답 헤더와 쿠키에 새 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey + " " + newAccessToken);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .path("/")
                .maxAge(3600)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .headers(headers)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(Map.of("accessToken", newAccessToken));
    }
}
