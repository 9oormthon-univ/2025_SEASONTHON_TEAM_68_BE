package org.zerock.groom_tone.domain.meeting.controller;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.groom_tone.domain.meeting.DTO.CategoryMeetingDTO;
import org.zerock.groom_tone.domain.meeting.controller.request.CalendarRequest;
import org.zerock.groom_tone.domain.meeting.controller.response.TodoResponse;
import org.zerock.groom_tone.domain.meeting.entity.TodoEntity;
import org.zerock.groom_tone.domain.meeting.service.MeetingService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    // 텍스트 받아서 겔린더에 올리기
    @PostMapping("/categorizing/text")
    public ResponseEntity<?> postCalender(@RequestBody CalendarRequest request) {
        CategoryMeetingDTO todoList = meetingService.categorizeMeetings(request.getContent());
        return ResponseEntity.ok(todoList);
    }

    // 미팅 전체 보기
    @GetMapping("/meeting")
    public ResponseEntity<?> getMeeting() {
        return ResponseEntity.ok(meetingService.getMeeting());
    }

    // 두투 전체보기
    @GetMapping("/todo")
    public ResponseEntity<?> getTodo() {
        List<TodoEntity> todoEntity = meetingService.getTodo();

        List<TodoResponse> responseList = todoEntity.stream()
                .map(v -> TodoResponse.builder()
                        .id(v.getId())
                        .tasks(v.getTasks())
                        .dueDates(v.getDueDates())
                        .personName(v.getPersonName())
                        .priority(v.getPriority())
                        .status(String.valueOf(v.getStatus()))
                        .isFinished(String.valueOf(v.getIsFinished()))
                        .build())
                .toList();

        return ResponseEntity.ok(responseList);
    }

    // 투두 완료하기
    @PostMapping("/todo/change")
    public ResponseEntity<?> completedTodo(@Parameter Long id) {
        TodoEntity todoEntity = meetingService.completedTodo(id);
        
        TodoResponse response = TodoResponse.builder()
                .id(todoEntity.getId())
                .tasks(todoEntity.getTasks())
                .dueDates(todoEntity.getDueDates())
                .personName(todoEntity.getPersonName())
                .priority(todoEntity.getPriority())
                .status(String.valueOf(todoEntity.getStatus()))
                .isFinished(String.valueOf(todoEntity.getIsFinished()))
                .build();

        return ResponseEntity.ok(response);
    }


}
