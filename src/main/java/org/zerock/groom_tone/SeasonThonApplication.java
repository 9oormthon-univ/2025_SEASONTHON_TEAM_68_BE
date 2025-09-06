package org.zerock.groom_tone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zerock.groom_tone.common.config.DotEnvConfig;

@SpringBootApplication
public class SeasonThonApplication {
    public static void main(String[] args) {
        // 환경 변수 로드
        DotEnvConfig.loadEnv();

        SpringApplication.run(SeasonThonApplication.class, args);
    }

}
