package org.zerock.groom_tone.domain.meeting.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoResponse {
    private Long id;
    private String personName;
    private String tasks;
    private String dueDates;
    private String status;
    private String isFinished;
    private Long priority;
}
