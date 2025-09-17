package org.zerock.groom_tone.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.groom_tone.domain.member.DTO.MemberDTO;
import org.zerock.groom_tone.domain.member.controller.request.LoginRequest;
import org.zerock.groom_tone.domain.member.controller.response.MemberResponse;
import org.zerock.groom_tone.domain.member.service.MemberService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> signup(@RequestBody LoginRequest request){

        MemberDTO memberDTO = MemberDTO.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        String sessionId = memberService.registerMember(memberDTO);

        MemberResponse response = MemberResponse.builder()
                .sessionId(sessionId)
                .build();

        return ResponseEntity.ok(response);
    }


    // 세션 재발급
}
