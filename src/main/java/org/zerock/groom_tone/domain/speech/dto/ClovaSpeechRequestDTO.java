package org.zerock.groom_tone.domain.speech.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ClovaSpeechRequestDTO {
    private String language = "ko-KR";
    private String completion = "sync";
    private String callback;
    private Map<String, Object> userdata;
    private Boolean wordAlignment = Boolean.TRUE;
    private Boolean fullText = Boolean.TRUE;
    private List<Boosting> boostings;
    private String forbiddens;
    private Diarization diarization;
    private Sed sed;
}
