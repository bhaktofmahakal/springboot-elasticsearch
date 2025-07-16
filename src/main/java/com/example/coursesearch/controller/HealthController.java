package com.example.coursesearch.controller;

import com.example.coursesearch.document.CourseDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final ElasticsearchOperations elasticsearchOperations;
    
    public HealthController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        try {
            // Checking if Elasticsearch is accessible
            boolean isElasticsearchHealthy = elasticsearchOperations.indexOps(CourseDocument.class).exists();
            
            Map<String, Object> health = Map.of(

                "status", isElasticsearchHealthy ? "UP" : "DOWN",
                "elasticsearch", isElasticsearchHealthy ? "UP" : "DOWN",
                "timestamp", System.currentTimeMillis()
                
            );
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            Map<String, Object> health = Map.of(

                "status", "DOWN",
                "elasticsearch", "DOWN",
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()

            );
            
            return ResponseEntity.status(503).body(health);
        }
    }
}