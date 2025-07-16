package com.example.coursesearch.performance;

import com.example.coursesearch.dto.SearchRequest;
import com.example.coursesearch.dto.SearchResponse;
import com.example.coursesearch.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SearchPerformanceTest {

    @Autowired
    private CourseService courseService;

    @Test
    public void testSearchPerformance() throws Exception {
        // Warm up
        SearchRequest warmupRequest = new SearchRequest();
        warmupRequest.setQ("math");
        warmupRequest.setSize(10);
        
        for (int i = 0; i < 10; i++) {
            courseService.searchCourses(warmupRequest);
        }

        // Performance test
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SearchRequest request = new SearchRequest();
        request.setQ("science");
        request.setMinAge(10);
        request.setMaxAge(18);
        request.setCategory("Science");
        request.setSort("priceAsc");
        request.setPage(0);
        request.setSize(20);

        SearchResponse response = courseService.searchCourses(request);

        stopWatch.stop();

        // Assertions
        assertNotNull(response);
        assertTrue(response.getTotal() >= 0);
        assertTrue(stopWatch.getTotalTimeMillis() < 1000, 
                   "Search should complete within 1 second. Actual: " + stopWatch.getTotalTimeMillis() + "ms");
        
        System.out.println("Search completed in: " + stopWatch.getTotalTimeMillis() + "ms");
    }

    @Test
    public void testConcurrentSearches() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<SearchResponse>> futures = new ArrayList<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Create 50 concurrent search requests
        for (int i = 0; i < 50; i++) {
            final int index = i;
            CompletableFuture<SearchResponse> future = CompletableFuture.supplyAsync(() -> {
                SearchRequest request = new SearchRequest();
                request.setQ("test" + index);
                request.setPage(index % 5);
                request.setSize(10);
                return courseService.searchCourses(request);
            }, executor);
            futures.add(future);
        }

        // Wait for all to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        stopWatch.stop();

        // Verify all requests succeeded
        for (CompletableFuture<SearchResponse> future : futures) {
            SearchResponse response = future.get();
            assertNotNull(response);
            assertTrue(response.getTotal() >= 0);
        }

        assertTrue(stopWatch.getTotalTimeMillis() < 5000, 
                   "50 concurrent searches should complete within 5 seconds. Actual: " + stopWatch.getTotalTimeMillis() + "ms");
        
        System.out.println("50 concurrent searches completed in: " + stopWatch.getTotalTimeMillis() + "ms");
        
        executor.shutdown();
    }

    @Test
    public void testSuggestionPerformance() throws Exception {
        // Warm up
        for (int i = 0; i < 5; i++) {
            courseService.getSuggestions("ma");
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> suggestions = courseService.getSuggestions("math");

        stopWatch.stop();

        assertNotNull(suggestions);
        assertTrue(stopWatch.getTotalTimeMillis() < 500, 
                   "Suggestions should complete within 500ms. Actual: " + stopWatch.getTotalTimeMillis() + "ms");
        
        System.out.println("Suggestions completed in: " + stopWatch.getTotalTimeMillis() + "ms");
    }
}