package com.example.coursesearch.config;

import com.example.coursesearch.document.CourseDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ElasticsearchIndexConfig {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexConfig.class);
    private final ElasticsearchOperations elasticsearchOperations;
    
    public ElasticsearchIndexConfig(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void createIndex() {
        try {
            IndexOperations indexOperations = elasticsearchOperations.indexOps(CourseDocument.class);
            
            if (!indexOperations.exists()) {
                // Create index 

                indexOperations.create();
                indexOperations.putMapping(indexOperations.createMapping());
                log.info("Created courses index with auto-generated mapping");
            } else {
                log.info("Courses index already exists");
            }
        } catch (Exception e) {
            
            log.error("Error creating index", e);
        }
    }
}