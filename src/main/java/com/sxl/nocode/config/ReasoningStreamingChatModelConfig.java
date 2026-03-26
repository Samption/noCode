package com.sxl.nocode.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
// @ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
@ConfigurationProperties(prefix = "langchain4j.open-ai.streaming-chat-model")
@Data
@Slf4j
public class ReasoningStreamingChatModelConfig {

    private String baseUrl;

    private String apiKey;

    private String modelName;

    private Integer maxTokens;

    /**
     * 推理流式模型（用于 Vue 项目生成，带工具调用）
     */
    @Bean
    public StreamingChatModel reasoningStreamingChatModel() {
        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .timeout(Duration.ofMinutes(10))
                .logRequests(false)
                .logResponses(false);
                
        // 若 API 支持，这会让模型读取并抛出 reasoning_content 作为 partialThinking
        // 安全调用 returnThinking(true)，兼容 boolean 和 Boolean 参数版本
        try {
            java.util.Arrays.stream(builder.getClass().getMethods())
                    .filter(m -> "returnThinking".equals(m.getName()) && m.getParameterCount() == 1)
                    .findFirst()
                    .ifPresent(m -> {
                        try {
                            m.invoke(builder, true);
                        } catch (Exception e) {
                            log.error("设置returnThinking失败：", e);
                        }
                    });
        } catch (Exception e) {
            log.error("设置returnThinking失败：", e);
        }

        return builder.build();
    }
}
