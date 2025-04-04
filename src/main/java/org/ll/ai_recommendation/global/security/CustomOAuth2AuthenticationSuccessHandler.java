package org.ll.ai_recommendation.global.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ll.ai_recommendation.domain.member.entity.Member;
import org.ll.ai_recommendation.domain.member.service.MemberService;
import org.ll.ai_recommendation.global.rq.Rq;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final MemberService memberService;
    private final Rq rq;
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Member actor = memberService.findById(rq.getActor().getId()).get();
        rq.makeAuthCookies(actor);
        String redirectUrl = request.getParameter("state");
        response.sendRedirect(redirectUrl);
    }
}