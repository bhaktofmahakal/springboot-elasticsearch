package com.example.coursesearch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
public class SearchControllerSimpleTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSearchEndpointExists() throws Exception {
        mockMvc.perform(get("/api/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSearchWithKeyword() throws Exception {
        mockMvc.perform(get("/api/search?q=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSearchWithFilters() throws Exception {
        mockMvc.perform(get("/api/search?category=Science&minAge=10&maxAge=15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSearchWithPagination() throws Exception {
        mockMvc.perform(get("/api/search?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.courses").isArray());
    }

    @Test
    public void testSuggestEndpoint() throws Exception {
        mockMvc.perform(get("/api/search/suggest?q=math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").exists());
    }
}