package org.zerock.groom_tone.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.zerock.groom_tone.domain.member.DTO.MemberDTO;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false, length = 256)
    private String sessionId;

    @Builder
    public MemberEntity(String email, String name, String sessionId) {
        this.email = email;
        this.name = name;
        this.sessionId = sessionId;
    }

    public void updateSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }
}
