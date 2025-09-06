package org.zerock.groom_tone.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.groom_tone.domain.member.DTO.NaverProfileDTO.UserInfo;
import org.zerock.groom_tone.domain.member.controller.response.MemberResponse;
import org.zerock.groom_tone.domain.member.service.MemberService;
import org.zerock.groom_tone.domain.member.service.NaverOauth2Service;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final NaverOauth2Service naverOauth2Service;
    private final MemberService memberService;

    @GetMapping()
    public ResponseEntity<MemberResponse> register(@RequestParam(name = "code") String code,
                                                   @RequestParam(name = "state") String state) {
        String accessToken = naverOauth2Service.getAccessToken(code, state);

        UserInfo userInfo = naverOauth2Service.getUserInfo(accessToken);

        String sessionId = memberService.registerMember(userInfo);

        MemberResponse response = MemberResponse.builder()
                .sessionId(sessionId)
                .build();

        return ResponseEntity.ok(response);
    }

    // 세션 재발급
}
