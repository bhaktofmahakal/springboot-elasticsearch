package com.example.coursesearch.dto;

import java.util.List;

public class SearchResponse {
    private long total;
    private List<CourseResponseDto> courses;
    
    // Constructors

    public SearchResponse() {}
    
    public SearchResponse(long total, List<CourseResponseDto> courses) {
        this.total = total;
        this.courses = courses;
    }
    
    // Getters and Setters
    
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    
    public List<CourseResponseDto> getCourses() { return courses; }
    public void setCourses(List<CourseResponseDto> courses) { this.courses = courses; }
}