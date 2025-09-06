package org.zerock.groom_tone.common.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final String message;
    private final LocalDateTime timestamp;
    private final String path;

    public static ErrorResponse of(String message, String path) {
        return ErrorResponse.builder()
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}
