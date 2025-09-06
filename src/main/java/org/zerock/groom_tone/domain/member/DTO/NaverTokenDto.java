package org.zerock.groom_tone.domain.member.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor // Jackson은 역직렬화를 위해 기본 생성자가 필요합니다.
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverTokenDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    // error, error_description 필드는 일부러 정의하지 않았습니다.
    // 성공 시에는 이 필드들이 JSON에 없지만, 위 설정 덕분에 오류 없이 매핑됩니다.
}
