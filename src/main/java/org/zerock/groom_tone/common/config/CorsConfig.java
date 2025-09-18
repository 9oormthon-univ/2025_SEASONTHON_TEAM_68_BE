package org.zerock.groom_tone.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("https://2025-seasonthon-team-68-fnzek0yxu-fine-pines-projects.vercel.app", "http://localhost:3000")
                        .allowedMethods("*") 
                        .allowCredentials(true);
            }
        };
    }
}
