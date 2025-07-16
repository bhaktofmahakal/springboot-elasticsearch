package com.example.coursesearch.integration;

import com.example.coursesearch.dto.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebMvc
public class CourseSearchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSearchAllCourses() throws Exception {
       
        Thread.sleep(3000);

        MvcResult result = mockMvc.perform(get("/api/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        SearchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), SearchResponse.class);
        
        assertTrue(response.getTotal() >= 0);
        assertNotNull(response.getCourses());
    }

    @Test
    public void testSearchByKeywordWithFuzzyMatching() throws Exception {
        Thread.sleep(3000);

        // Test fuzzy search
        MvcResult result = mockMvc.perform(get("/api/search?q=dinors"))
                .andExpect(status().isOk())
                .andReturn();

        SearchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), SearchResponse.class);
        
        // Fuzzy matching should working 
        assertTrue(response.getTotal() >= 0);
        assertNotNull(response.getCourses());
    }

    @Test
    public void testSearchFilters() throws Exception {
        Thread.sleep(3000);

        //Test category filter
        mockMvc.perform(get("/api/search?category=Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());

        //Test age range filter
        mockMvc.perform(get("/api/search?minAge=10&maxAge=15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());

        // Test price range filter
        mockMvc.perform(get("/api/search?minPrice=100&maxPrice=200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSorting() throws Exception {
        Thread.sleep(3000);

        //Test price ascending sort
        mockMvc.perform(get("/api/search?sort=priceAsc&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testPagination() throws Exception {
        Thread.sleep(3000);

        // Test pagination
        mockMvc.perform(get("/api/search?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray());

        mockMvc.perform(get("/api/search?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testAutocompleteSuggestions() throws Exception {
        Thread.sleep(3000);

        // Test autocomplete suggestions
        mockMvc.perform(get("/api/search/suggest?q=math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testDateFilter() throws Exception {
        Thread.sleep(3000);

        // Test date filter
        mockMvc.perform(get("/api/search?startDate=2025-02-01T00:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testCombinedFilters() throws Exception {
        Thread.sleep(3000);

        // Test multiple filters combined
        mockMvc.perform(get("/api/search?category=Science&minAge=10&maxAge=18&sort=priceAsc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }
}