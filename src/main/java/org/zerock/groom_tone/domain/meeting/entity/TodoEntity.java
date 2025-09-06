package org.zerock.groom_tone.domain.meeting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TodoEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @Column(nullable = false)
    private String personName;

    @Column(nullable = false)
    private String tasks;

    @Column(nullable = false)
    private String dueDates;

    @Enumerated(EnumType.STRING)
    private TodoStatus status;

    @Column()
    private LocalDateTime isFinished;

    @Column(nullable = false)
    private Long priority;

    @Builder
    public TodoEntity(MeetingEntity meetingEntity, String personName, String tasks, String dueDates, Long priority) {
        this.meeting = meetingEntity;
        this.personName = personName;
        this.tasks = tasks;
        this.dueDates = dueDates;
        this.priority = priority;
        this.status = TodoStatus.IN_PROGRESS;
        this.isFinished = null;
    }

    public void changeStatus() {
        if (this.status == TodoStatus.IN_PROGRESS) {
            this.status = TodoStatus.COMPLETED;
            this.isFinished = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        } else {
            this.status = TodoStatus.IN_PROGRESS;
            this.isFinished = null;
        }
    }

}
