package com.example.coursesearch.controller;

import com.example.coursesearch.dto.SearchRequest;
import com.example.coursesearch.dto.SearchResponse;
import com.example.coursesearch.service.CourseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/search")

public class SearchController {

    private final CourseService courseService;
    
    public SearchController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<SearchResponse> searchCourses(

            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        SearchRequest request = new SearchRequest(q, minAge, maxAge, category, type, 
                                                minPrice, maxPrice, startDate, sort, page, size);
        
        // Validating the request

        request.validate();
        
        SearchResponse response = courseService.searchCourses(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String q) {
        
        List<String> suggestions = courseService.getSuggestions(q);
        return ResponseEntity.ok(suggestions);
    }
}