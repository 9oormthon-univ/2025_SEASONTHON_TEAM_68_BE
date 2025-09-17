package org.zerock.groom_tone.domain.member.controller.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String name;
}
