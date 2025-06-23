package com.aelure.service;

import com.aelure.client.*;
import com.aelure.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final PythonEmbeddingClient embeddingClient;
    private final ScraperClient scraperClient;

    public SearchService(PythonEmbeddingClient embeddingClient, ScraperClient scraperClient) {
        this.embeddingClient = embeddingClient;
        this.scraperClient = scraperClient;
    }

    public List<FashionItem> searchWebByText(String query) {
        float[] queryEmbedding = embeddingClient.getTextEmbedding(query);
        List<FashionItem> scrapedItems = scraperClient.scrapeFashionSites(query);
        return rankBySimilarity(scrapedItems, queryEmbedding);
    }

    public List<FashionItem> searchWebByImage(MultipartFile file) {
        float[] queryEmbedding = embeddingClient.getImageEmbedding(file);

    }

    private List<FashionItem> rankBySimilarity(List<FashionItem> items, float[] queryEmbedding) {
        return items.stream()
                .filter(item -> item.getEmbedding() != null)
                .sorted(Comparator.comparingDouble(
                        item -> -cosineSimilarity(queryEmbedding, item.getEmbedding())))
                .limit(10)
                .collect(Collectors.toList());
    }

    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}