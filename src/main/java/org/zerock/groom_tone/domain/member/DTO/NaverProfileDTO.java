package org.zerock.groom_tone.domain.member.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverProfileDTO {

    private String resultcode;
    private String message;
    private UserInfo response;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {

        private String id;
        private String nickname;
        private String name;
        private String email;
    }
}
