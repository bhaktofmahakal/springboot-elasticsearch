package com.example.coursesearch.controller;

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
public class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSearchAllCourses() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        SearchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(), SearchResponse.class);
        
        assertTrue(response.getTotal() > 0);
        assertFalse(response.getCourses().isEmpty());
    }

    @Test
    public void testSearchByKeyword() throws Exception {
        mockMvc.perform(get("/api/search?q=math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSearchByCategory() throws Exception {
        mockMvc.perform(get("/api/search?category=Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSearchWithPagination() throws Exception {
        mockMvc.perform(get("/api/search?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses.length()").value(5));
    }

    @Test
    public void testAutocompleteSuggestions() throws Exception {
        mockMvc.perform(get("/api/search/suggest?q=math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }
}