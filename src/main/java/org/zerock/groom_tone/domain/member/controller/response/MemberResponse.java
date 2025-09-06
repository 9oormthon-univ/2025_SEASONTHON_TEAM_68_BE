package org.zerock.groom_tone.domain.member.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private String sessionId;
}
