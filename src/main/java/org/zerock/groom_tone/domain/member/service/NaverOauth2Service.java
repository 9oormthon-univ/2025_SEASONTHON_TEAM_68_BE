package org.zerock.groom_tone.domain.member.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.zerock.groom_tone.domain.member.DTO.NaverProfileDTO;
import org.zerock.groom_tone.domain.member.DTO.NaverProfileDTO.UserInfo;
import org.zerock.groom_tone.domain.member.DTO.NaverTokenDto;

@Service
@Log4j2
public class NaverOauth2Service {
    @Value("${naver.client-id}")
    private String clientId;
    @Value("${naver.client-secret}")
    private String clientSecret;


    public String getAccessToken(String code, String state) {
        String reqUrl = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();

        // HttpBody Object
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", state);

        // http body params 와 http headers 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        // reqUrl로 Http 요청, POST 방식
        ResponseEntity<NaverTokenDto> responseBody = restTemplate.exchange(reqUrl,
                HttpMethod.POST,
                naverTokenRequest,
                NaverTokenDto.class);

        return responseBody.getBody().getAccessToken();
    }

    public UserInfo getUserInfo(String accessToken) {
        String reqUrl = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // 헤더만 포함된 HttpEntity 오브젝트 생성 (GET 요청이므로 바디는 없음)
        HttpEntity<Void> naverProfileRequest = new HttpEntity<>(headers);

        // 💡 HTTP 메소드를 GET으로 변경하고, 응답을 NaverApiResponse DTO로 받음
        ResponseEntity<NaverProfileDTO> response = restTemplate.exchange(
                reqUrl,
                HttpMethod.GET,
                naverProfileRequest,
                NaverProfileDTO.class
        );

        // DTO에서 실제 프로필 정보만 추출하여 반환
        NaverProfileDTO responseBody = response.getBody();

        return responseBody.getResponse();
    }
}
