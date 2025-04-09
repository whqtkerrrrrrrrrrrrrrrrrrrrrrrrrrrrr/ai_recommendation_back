package org.ll.ai_recommendation.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.ll.ai_recommendation.domain.member.entity.Member;
import org.ll.ai_recommendation.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthTokenService authTokenService;

    public Optional<Member> findByApiKey(String apiKey) {
        return memberRepository.findByApiKey(apiKey);
    }
    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public Optional<Member> findById(long authorId) {
        return memberRepository.findById(authorId);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public Member signup(String username, String password, String nickname, String avatar) {
        memberRepository
                .findByUsername(username)
                .ifPresent(user -> {
                    throw new ServiceException("해당 username은 이미 사용중입니다.");
                });

        Member member = memberRepository.save(Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .apiKey(UUID.randomUUID().toString())
                .build());

        return member;
    }

    @Transactional
    public Member modifyOrJoin(String username, String nickname, String avatar) {
        Optional<Member> opMember = findByUsername(username);
        return opMember.orElseGet(() -> signup(username, "", nickname, avatar));
    }
}
