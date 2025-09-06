package org.zerock.groom_tone.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.groom_tone.domain.meeting.entity.MeetingEntity;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
}
