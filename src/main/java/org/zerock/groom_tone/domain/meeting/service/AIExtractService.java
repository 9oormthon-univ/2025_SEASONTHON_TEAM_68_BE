package org.zerock.groom_tone.domain.meeting.service;


import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.groom_tone.domain.meeting.DTO.CategoryMeetingDTO;

@Log4j2
@Service
@RequiredArgsConstructor
public class AIExtractService {
    private final OpenAIClient openAIClient;

    private final String prompt = """
            **역할:** 당신은 회의록을 분석하여 할 일 목록(To-do list)과 구글 캘린더 일정을 생성하는 매우 유능한 AI 어시스턴트입니다.
            **지시사항:**
            1.  주어진 `<회의록>` 내용을 면밀히 분석합니다.
            2.  회의록에서 논의된 내용과 결정 사항을 바탕으로 다음 세 가지를 추출합니다.
                *   **전체 회의 내용을 요약(summary):** 회의에서 언급된 내용을 정리하여 요약합니다.
                *   **할 일 목록 (todoList):** 회의에서 발생한 실행 항목(Action Item)들을 명확하고 간결한 목록으로 만듭니다. 할 일이 없으면 null 값으로 처리한다. 회의에 참석한 인원들로 구성하며 각 항목은 '담당자', '업무 내용' 을 포함해야 합니다. 각 목록에는 1부터 5 까지 우선 순위를 부여하세요.
                *   **캘린더 일정 (calendarEvent):** 회의록에 언급된 '다음 회의'나 '새로운 일정'을 바탕으로 구글 캘린더에 등록할 일정을 생성합니다. 일정 제목, 설명, 장소, 시작 및 종료 시간, 참석자 이메일을 정확히 추출해야 합니다. 만약 새로운 일정이 없다면, 현재 회의 내용을 요약하여 등록합니다.
            3.  추출한 정보는 반드시 아래에 정의된 **JSON 형식**으로만 출력해야 합니다. 다른 설명이나 추가 텍스트 없이 JSON 객체만 반환하세요.
            4.  시간 정보는 `YYYY-MM-DDTHH:MM:SS` 형식의 ISO 8601 표준을 따라야 합니다.
            5.  회의록에서 특정 정보(예: 장소, 참석자)를 찾을 수 없다면, 해당 필드는 `null` 또는 빈 값으로 처리하세요.
            6.  현재 날짜는 {current_date} 입니다. 기한이 '내일까지'와 같이 상대적인 시간으로 표현되면, 현재 날짜를 기준으로 계산하여 절대적인 날짜로 변환하세요.
            """;


    public CategoryMeetingDTO extractToDosFromText(String text) {
        try {
            StructuredChatCompletionCreateParams<CategoryMeetingDTO> createParams = ChatCompletionCreateParams.builder()
                    .model(ChatModel.GPT_5_MINI)
                    .maxCompletionTokens(2048)
                    .responseFormat(CategoryMeetingDTO.class)
                    .addAssistantMessage(prompt)
                    .addUserMessage(text)
                    .build();
            return openAIClient.chat().completions().create(createParams).choices()
                    .getFirst().message().content()
                    .orElseThrow(() -> new RuntimeException("AI 응답 값이 비어있음"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("AI 생성중 오류");
        }
    }
}
