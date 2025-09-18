package org.zerock.groom_tone.domain.speech.controller.response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeechResponse {

    private static final Gson gson = new Gson();

    // 전체 회의 내용 (모든 발화 합친 텍스트)
    private String fullText;

    // 화자별 발화 리스트
    private List<SpeechResponse.SpeakerSegment> segments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpeakerSegment {
        private String speaker; // 화자명 (예: A, B, C)
        private String text;    // 해당 화자의 문장
    }

    public static SpeechResponse success(String json){
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // 전체 텍스트 추출
        String fullText = jsonObject.get("text").getAsString();

        // 화자별 segments 추출
        JsonArray segmentsArray = jsonObject.getAsJsonArray("segments");
        List<SpeakerSegment> segments = new ArrayList<>();

        segmentsArray.forEach(element -> {
            JsonObject seg = element.getAsJsonObject();
            String speaker = seg.getAsJsonObject("speaker").get("name").getAsString();
            String text = seg.get("textEdited").getAsString();
            segments.add(new SpeakerSegment(speaker, text));
        });

        return new SpeechResponse(fullText, segments);
    }
}
