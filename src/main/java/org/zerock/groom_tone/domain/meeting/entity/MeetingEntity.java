package org.zerock.groom_tone.domain.meeting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MeetingEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String rawText;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public MeetingEntity(String rawText, String summary) {
        this.rawText = rawText;
        this.summary = summary;
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    }


}
