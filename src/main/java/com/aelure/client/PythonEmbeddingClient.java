package com.aelure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Component
public class PythonEmbeddingClient {

    private final WebClient webClient;

    public PythonEmbeddingClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8000").build();
    }

    public float[] getTextEmbedding(String text) {
        return webClient.post()
                .uri("/embed/text")
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(float[].class)
                .block();
    }

    public float[] getImageEmbedding(MultipartFile file) {
        return webClient.post()
                .uri("/embed/image")
                .body(BodyInserters.fromMultipartData("file", file.getResource()))
                .retrieve()
                .bodyToMono(float[].class)
                .block();
    }
}