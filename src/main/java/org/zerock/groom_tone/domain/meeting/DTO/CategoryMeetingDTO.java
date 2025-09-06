package org.zerock.groom_tone.domain.meeting.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * 구글 캘린더 api 연결을 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryMeetingDTO {
    @JsonPropertyDescription("전체 회의 내용 요약")
    private String summary;

    @JsonProperty("calendar_events")
    private List<CalendarEvent> calendarEvents;

    @JsonProperty("todos_by_person")
    private List<PersonToDo> todosByPerson;


    /**
     * 추출된 캘린더 일정 정보를 담는 클래스
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalendarEvent {
        @JsonPropertyDescription("일정의 제목")
        private String title;
        
        @JsonPropertyDescription("일정에 대한 상세 설명")
        private String description;

        @JsonPropertyDescription("일정이 진행되는 장소")
        private String location;

        private EventDateTime start;
        private EventDateTime end;
        private List<EventAttendee> attendees;
    }

    /**
     * 사람별 할 일 목록을 담는 클래스
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonToDo {
        @JsonProperty("person_name")
        @JsonPropertyDescription("담당자의 이름")
        private String personName;

        @JsonProperty("tasks")
        @JsonPropertyDescription("담당자가 해야 할 일 목록")
        private List<String> tasks;

        @JsonProperty("due_dates")
        @JsonPropertyDescription("담당자가 해야 할 일의 기한")
        private String dueDates;

        @JsonPropertyDescription("1부터 5까지 우선 순위 입력")
        private String priority;
    }

    // --- 공용 중첩 클래스 ---

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventDateTime {
        @JsonProperty("date_time")
        @JsonPropertyDescription("날짜와 시간 (ISO 8601 형식). 예: 2025-09-10T10:00:00")
        private String dateTime;

        @JsonProperty("time_zone")
        @JsonPropertyDescription("타임존. 예: Asia/Seoul")
        private String timeZone = "Asia/Seoul";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventAttendee {
        @JsonPropertyDescription("참석자의 이메일 주소")
        private String email;
    }

}
