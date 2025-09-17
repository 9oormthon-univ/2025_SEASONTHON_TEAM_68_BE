package org.zerock.groom_tone.domain.meeting.service;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.groom_tone.domain.meeting.DTO.CategoryMeetingDTO;
import org.zerock.groom_tone.domain.meeting.DTO.CategoryMeetingDTO.PersonToDo;
import org.zerock.groom_tone.domain.meeting.entity.MeetingEntity;
import org.zerock.groom_tone.domain.meeting.entity.TodoEntity;
import org.zerock.groom_tone.domain.meeting.repository.MeetingRepository;
import org.zerock.groom_tone.domain.meeting.repository.TodoRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class MeetingService {

    private final AIExtractService aiExtractService;
    private final MeetingRepository meetingRepository;
    private final TodoRepository todoRepository;

    /**
     * 긴 회의록 텍스트에서 불필요한 공백, 탭, 과도한 줄바꿈 등을 제거하여 정제합니다. AI가 텍스트를 더 잘 이해하고 처리할 수 있도록 돕는 전처리 과정입니다.
     *
     * @param rawText 원본 회의록 텍스트
     * @return 정제된 텍스트
     */
    private String cleanMeetingTranscript(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return "";
        }

        // 탭(tab) 문자를 공백으로 일괄 변환
        String cleanedText = rawText.replace('\t', ' ');

        // 3개 이상의 연속된 줄바꿈을 2개로 줄여 문단 사이의 과도한 간격을 제거
        cleanedText = cleanedText.replaceAll("(\r\n|\r|\n){3,}", "\n\n");

        // 각 줄을 순회하며 불필요한 부분을 처리
        cleanedText = Arrays.stream(cleanedText.split("\r\n|\r|\n"))
                .map(String::trim) // 각 줄의 시작과 끝에 있는 공백 제거
                .filter(line -> !line.isEmpty()) // 내용이 없는 빈 줄은 완전히 제거
                .collect(Collectors.joining("\n"));

        return cleanedText;
    }

    // 미팅 저장
    public MeetingEntity saveMeeting(String rawText, String summary) {
        MeetingEntity meetingEntity = MeetingEntity.builder()
                .rawText(rawText)
                .summary(summary)
                .build();
        return meetingRepository.save(meetingEntity);
    }

    // 투두 저장
    public void saveTodo(MeetingEntity meetingEntity, PersonToDo personToDo) {
        for (String task : personToDo.getTasks()) {
            TodoEntity todoEntity = TodoEntity.builder()
                    .meetingEntity(meetingEntity)
                    .tasks(task)
                    .dueDates(personToDo.getDueDates())
                    .personName(personToDo.getPersonName())
                    .priority(Long.valueOf(personToDo.getPriority()))
                    .build();
            todoRepository.save(todoEntity);
        }
    }


    // 미팅 분류 등록
    public CategoryMeetingDTO categorizeMeetings(String rawText) {
        String cleanText = cleanMeetingTranscript(rawText);

        CategoryMeetingDTO categoryMeetingDTO = aiExtractService.extractToDosFromText(cleanText);
        log.info(categoryMeetingDTO);

        MeetingEntity meetingEntity = saveMeeting(cleanText, categoryMeetingDTO.getSummary());

        if (categoryMeetingDTO.getTodosByPerson() != null) {
            for (PersonToDo personToDo : categoryMeetingDTO.getTodosByPerson()) {
                saveTodo(meetingEntity, personToDo);
            }
        }

        return categoryMeetingDTO;
    }

    // 요약본 보기
    public List<MeetingEntity> getMeeting() {
        return meetingRepository.findAll();
    }


    // 투두 보기
    public List<TodoEntity> getTodo() {
        return todoRepository.findAll();
    }

    // 투두 상태 변경
    @Transactional
    public TodoEntity completedTodo(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("투두리스트의 id 가 잘못 되었습니다."));

        todoEntity.changeStatus();
        return todoRepository.save(todoEntity);
    }
}
