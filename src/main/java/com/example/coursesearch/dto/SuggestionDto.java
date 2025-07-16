package com.example.coursesearch.dto;

public class SuggestionDto {

    private String text;
    private String category;
    
    // Constructors
    public SuggestionDto() {}
    
    public SuggestionDto(String text, String category) {
        this.text = text;
        this.category = category;
    }
    
    // Getters and Setters
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}