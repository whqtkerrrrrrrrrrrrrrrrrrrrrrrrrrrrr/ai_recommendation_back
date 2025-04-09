package org.ll.ai_recommendation.domain.member.service;

import org.ll.ai_recommendation.domain.member.entity.Member;
import org.ll.ai_recommendation.global.Ut.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expirationSeconds}")
    private long accessTokenExpirationSeconds;

    // accessToken 생성
    String genAccessToken(Member member) {
        long id = member.getId();
        String username = member.getUsername();

        // 토큰 생성
        return Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of("id", id, "username", username)
        );
    }

    // JWT 토큰의 payload 검증 및 파싱
    Map<String, Object> payload(String accessToken) {
        // payload 파싱
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);

        // 유효하지 않은 토큰이라면 null 리턴
        if (parsedPayload == null) return null;

        long id = (long) (Integer) parsedPayload.get("id");
        String username = (String) parsedPayload.get("username");

        return Map.of("id", id, "username", username);
    }
}
