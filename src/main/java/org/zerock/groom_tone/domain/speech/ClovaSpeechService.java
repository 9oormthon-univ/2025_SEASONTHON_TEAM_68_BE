package org.zerock.groom_tone.domain.speech;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ClovaSpeechService {

    // Clova Speech secret key
    @Value("${naver.speech-secret}")
    private String SECRET;
    // Clova Speech invoke URL
    @Value("${naver.speech-invoke}")
    private String INVOKE_URL;

    private final CloseableHttpClient httpClient;
    private final Gson gson;

    public ClovaSpeechService() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
    }

    @Data
    public static class Boosting {
        private String words;
    }

    @Data
    public static class Diarization {
        private Boolean enable = Boolean.FALSE;
        private Integer speakerCountMin;
        private Integer speakerCountMax;
    }

    @Data
    public static class Sed {
        private Boolean enable = Boolean.FALSE;
    }

    @Data
    public static class NestRequestEntity {
        private String language = "ko-KR";
        private String completion = "sync";
        private String callback;
        private Map<String, Object> userdata;
        private Boolean wordAlignment = Boolean.TRUE;
        private Boolean fullText = Boolean.TRUE;
        private List<Boosting> boostings;
        private String forbiddens;
        private Diarization diarization;
        private Sed sed;
    }

    /**
     * 로컬 파일 업로드 후 음성 인식을 요청합니다.
     * @param file 필수, 음성 파일
     * @param nestRequestEntity 옵션, 인식 옵션
     * @return String API 응답 결과
     */
    public String upload(File file, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/upload");

        // SECRET 키가 주입된 후 헤더를 동적으로 생성
        Header[] headers = new Header[] {
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("X-CLOVASPEECH-API-KEY", SECRET),
        };
        httpPost.setHeaders(headers);

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("params", gson.toJson(nestRequestEntity), ContentType.APPLICATION_JSON)
                .addBinaryBody("media", file, ContentType.MULTIPART_FORM_DATA, file.getName())
                .build();
        httpPost.setEntity(httpEntity);

        return execute(httpPost);
    }

    private String execute(HttpPost httpPost) {
        try (final CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();

            return entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;
        } catch (IOException e) {
            // 더 명확한 예외 메시지와 함께 RuntimeException으로 전환
            throw new IllegalStateException("Clova Speech API 요청 실행에 실패했습니다.", e);
        }
    }


    /**
     * MultipartFile을 java.io.File로 변환합니다.
     * 시스템의 임시 디렉토리에 파일을 생성합니다.
     * @param multipartFile 변환할 MultipartFile
     * @return 변환된 File 객체
     * @throws IOException 파일 쓰기 중 오류 발생 시
     */
    public File convertMultipartFileToFile(MultipartFile multipartFile)  {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }
        // 원본 파일 이름을 유지하여 임시 파일을 생성합니다.
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
            multipartFile.transferTo(convFile);
            return convFile;
        }catch (IOException e){
            throw new RuntimeException("파일 변환 중 오류 발생");
        }

    }

    public NestRequestEntity setDiarization(Integer min,Integer max){
        NestRequestEntity requestEntity = new NestRequestEntity();
        // 화자 분리 기능 활성화
        ClovaSpeechService.Diarization diarization = new ClovaSpeechService.Diarization();
        diarization.setEnable(true);
        diarization.setSpeakerCountMin(min);
        diarization.setSpeakerCountMax(max);
        requestEntity.setDiarization(diarization);

        return requestEntity;
    }

}
