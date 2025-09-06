package org.zerock.groom_tone.domain.meeting.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingResponse {
    private Long id;
    private String rawText;
    private String summary;
    private String createdAt;
}
