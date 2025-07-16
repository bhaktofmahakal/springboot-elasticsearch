package com.example.coursesearch.service;

import com.example.coursesearch.document.CourseDocument;
import com.example.coursesearch.dto.CourseResponseDto;
import com.example.coursesearch.dto.SearchRequest;

import com.example.coursesearch.dto.SearchResponse;
import com.example.coursesearch.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.SortOrder;

import co.elastic.clients.json.JsonData;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    
    private final CourseRepository courseRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper;
    
    public CourseService(CourseRepository courseRepository, 
                        ElasticsearchOperations elasticsearchOperations, 
                        ObjectMapper objectMapper) {

        this.courseRepository = courseRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initData() {
        try {
            // Check if index already has data
            if (courseRepository.count() > 0) {

                log.info("Courses index already contains data, skipping initialization");
                return;

            }

            ClassPathResource resource = new ClassPathResource("sample-courses.json");

            List<Map<String, Object>> coursesData = objectMapper.readValue(
                resource.getInputStream(), new TypeReference<List<Map<String, Object>>>() {}
            );

            List<CourseDocument> courses = new ArrayList<>();

            for (Map<String, Object> courseData : coursesData) {

                CourseDocument course = new CourseDocument();
                course.setId((String) courseData.get("id"));
                course.setTitle((String) courseData.get("title"));
                course.setDescription((String) courseData.get("description"));
                course.setCategory((String) courseData.get("category"));
                course.setType(CourseDocument.CourseType.valueOf((String) courseData.get("type")));
                course.setGradeRange((String) courseData.get("gradeRange"));
                course.setMinAge((Integer) courseData.get("minAge"));
                course.setMaxAge((Integer) courseData.get("maxAge"));
                course.setPrice(((Number) courseData.get("price")).doubleValue());
                course.setNextSessionDate(Instant.parse((String) courseData.get("nextSessionDate")));
                
                // Set up completion field for autocomplete
                course.setSuggest(new Completion(new String[]{course.getTitle()}));
                
                courses.add(course);
            }

            courseRepository.saveAll(courses);
            log.info("Successfully indexed {} courses", courses.size());

        } catch (IOException e) {
            log.error("Error loading sample data", e);
        }
    }

    public SearchResponse searchCourses(SearchRequest request) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // Full-text search on title and description
        if (request.getQ() != null && !request.getQ().trim().isEmpty()) {
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(m -> m
                .fields("title", "description")
                .query(request.getQ())
                .fuzziness("AUTO")
            );
            boolQueryBuilder.must(multiMatchQuery._toQuery());
        }

        // Age range filter
        if (request.getMinAge() != null || request.getMaxAge() != null) {

            RangeQuery.Builder minAgeRangeBuilder = new RangeQuery.Builder().field("minAge");
            RangeQuery.Builder maxAgeRangeBuilder = new RangeQuery.Builder().field("maxAge");
            
            if (request.getMinAge() != null) {
                maxAgeRangeBuilder.gte(JsonData.of(request.getMinAge()));
            }
            if (request.getMaxAge() != null) {
                minAgeRangeBuilder.lte(JsonData.of(request.getMaxAge()));
            }
            
            boolQueryBuilder.filter(minAgeRangeBuilder.build()._toQuery());
            boolQueryBuilder.filter(maxAgeRangeBuilder.build()._toQuery());
        }

        // Category filter

        if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
            TermQuery categoryQuery = TermQuery.of(t -> t
                .field("category")
                .value(request.getCategory())
            );
            boolQueryBuilder.filter(categoryQuery._toQuery());
        }

        // Type filter

        if (request.getType() != null && !request.getType().trim().isEmpty()) {
            TermQuery typeQuery = TermQuery.of(t -> t
                .field("type")
                .value(request.getType())
            );
            boolQueryBuilder.filter(typeQuery._toQuery());
        }

        // Price range filter

        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            RangeQuery.Builder priceRangeBuilder = new RangeQuery.Builder().field("price");
            
            if (request.getMinPrice() != null) {
                priceRangeBuilder.gte(JsonData.of(request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                priceRangeBuilder.lte(JsonData.of(request.getMaxPrice()));
            }
            
            boolQueryBuilder.filter(priceRangeBuilder.build()._toQuery());
        }

        // Start date filter

        if (request.getStartDate() != null) {
            RangeQuery dateQuery = RangeQuery.of(r -> r
                .field("nextSessionDate")
                .gte(JsonData.of(request.getStartDate().toString()))
            );
            boolQueryBuilder.filter(dateQuery._toQuery());
        }

        // Build the query

        Query query = boolQueryBuilder.build()._toQuery();
        
        // Create pageable

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        
        // Build nat query with sorting

        NativeQuery nativeQuery;
        
        // Apply sorting

        switch (request.getSort()) {
            case "priceAsc":
                nativeQuery = NativeQuery.builder()
                    .withQuery(query)
                    .withPageable(pageable)
                    .withSort(s -> s.field(f -> f.field("price").order(SortOrder.Asc)))
                    .build();
                break;
            case "priceDesc":
                nativeQuery = NativeQuery.builder()
                    .withQuery(query)
                    .withPageable(pageable)
                    .withSort(s -> s.field(f -> f.field("price").order(SortOrder.Desc)))
                    .build();
                break;
            case "upcoming":
            default:
                nativeQuery = NativeQuery.builder()
                    .withQuery(query)
                    .withPageable(pageable)
                    .withSort(s -> s.field(f -> f.field("nextSessionDate").order(SortOrder.Asc)))
                    .build();
                break;
        }

        SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(nativeQuery, CourseDocument.class);
        
        List<CourseResponseDto> courses = searchHits.getSearchHits().stream()
            .map(SearchHit::getContent)
            .map(this::convertToDto)
            .collect(Collectors.toList());

        return new SearchResponse(searchHits.getTotalHits(), courses);
    }

    public List<String> getSuggestions(String query) {
        try {
           
            NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                    .should(s -> s.prefix(p -> p.field("title").value(query.toLowerCase())))
                    .should(s -> s.match(m -> m.field("title").query(query).fuzziness("AUTO")))
                ))
                .withPageable(PageRequest.of(0, 10))
                .build();

            SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(nativeQuery, CourseDocument.class);
            
            return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(CourseDocument::getTitle)
                .distinct()
                .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error getting suggestions", e);
            return new ArrayList<>();
        }
    }

    private CourseResponseDto convertToDto(CourseDocument course) {
        return new CourseResponseDto(
            course.getId(),
            course.getTitle(),
            course.getDescription(),
            course.getCategory(),
            course.getType().name(),
            course.getGradeRange(),
            course.getMinAge(),
            course.getMaxAge(),
            course.getPrice(),
            course.getNextSessionDate()
        );
    }
}