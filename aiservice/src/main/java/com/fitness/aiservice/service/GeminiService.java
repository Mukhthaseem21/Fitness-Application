package com.fitness.aiservice.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

@Service
public class GeminiService {
    private final WebClient webClient;

    public GeminiService(
        WebClient.Builder webClientBuilder,
        @Value("${gemini.api.url}") String geminiApiUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(geminiApiUrl).build();
    }

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of(
            "contents", new Object[] {
                Map.of("parts", new Object[] {
                    Map.of("text", question)
                })
            }
        );

        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", geminiApiKey).build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
