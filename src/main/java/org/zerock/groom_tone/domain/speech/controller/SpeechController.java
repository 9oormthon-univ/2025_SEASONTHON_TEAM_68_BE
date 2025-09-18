package org.zerock.groom_tone.domain.speech.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.groom_tone.domain.speech.ClovaSpeechService;
import org.zerock.groom_tone.domain.speech.controller.response.SpeechResponse;
import org.zerock.groom_tone.domain.speech.dto.ClovaSpeechRequestDTO;

import java.io.File;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class SpeechController {

    private final ClovaSpeechService clovaSpeechService;

    @PostMapping("/categorizing/speech")
    public ResponseEntity<SpeechResponse> recognizeSpeech(@RequestParam("audio") MultipartFile audioFile) {

        File tempFile = null;

        // 1. MultipartFile을 임시 File 객체로 변환
        tempFile = clovaSpeechService.convertMultipartFileToFile(audioFile);

        // 2. Clova Speech API 요청에 필요한 기본 옵션 객체 생성
        ClovaSpeechRequestDTO requestDTO = clovaSpeechService.setDiarization(1, 6);

        // 3. 서비스 레이어를 호출하여 음성 인식 실행
        String recognitionResult = clovaSpeechService.upload(tempFile, requestDTO);

        // 4. 임시 파일 삭제
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }

        // 5. 결과 반환
        SpeechResponse response = SpeechResponse.success(recognitionResult);

        return ResponseEntity.ok(response);

    }

}
