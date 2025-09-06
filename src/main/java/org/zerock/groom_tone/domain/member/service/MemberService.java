package org.zerock.groom_tone.domain.member.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.groom_tone.domain.member.DTO.NaverProfileDTO.UserInfo;
import org.zerock.groom_tone.domain.member.entity.MemberEntity;
import org.zerock.groom_tone.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 저장
    public String saveMember(UserInfo userInfo) {
        String sessionId = registerSession();

        MemberEntity memberEntity = MemberEntity.builder()
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .sessionId(sessionId)
                .build();

        memberRepository.save(memberEntity);
        return sessionId;
    }

    // 중복 확인
    public boolean duplicateMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 분기 - 아이디가 잇으면 새션 다시 발급 없으면 저장 후 세션 발급
    @Transactional
    public String registerMember(UserInfo userInfo) {
        if (duplicateMember(userInfo.getEmail())) { // 이미 가입된 유저
            return ReRegisterSession(userInfo.getEmail());
        }// 가입 안 된 유저
        return saveMember(userInfo);
    }

    public String ReRegisterSession(String email) {
        String sessionId = registerSession();
        MemberEntity memberEntity = memberRepository.findByEmail(email);
        memberEntity.updateSession(sessionId);
        return sessionId;
    }

    // 새션 생성
    public String registerSession() {
        return UUID.randomUUID().toString();
    }

}
