package org.zerock.groom_tone.domain.speech.dto;

import lombok.Data;

@Data
public class Diarization {
    private Boolean enable = Boolean.FALSE;
    private Integer speakerCountMin;
    private Integer speakerCountMax;
}
