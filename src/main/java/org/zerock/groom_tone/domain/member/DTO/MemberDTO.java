package org.zerock.groom_tone.domain.member.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDTO {
    private String email;
    private String name;
}
