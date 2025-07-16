package com.example.coursesearch.dto;

import java.time.Instant;

public class SearchRequest {
    private String q;
    private Integer minAge;
    private Integer maxAge;
    private String category;
    private String type;
    private Double minPrice;
    private Double maxPrice;
    private Instant startDate;
    private String sort = "upcoming";
    private int page = 0;
    private int size = 10;
    
    // Constructors
    public SearchRequest() {}
    
    public SearchRequest(String q, Integer minAge, Integer maxAge, String category, String type, 
                        Double minPrice, Double maxPrice, Instant startDate, String sort, int page, int size) {
        this.q = q;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.category = category;
        this.type = type;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.startDate = startDate;
        this.sort = sort;
        this.page = page;
        this.size = size;
    }
    
    // Getters and Setters
    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
    
    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }
    
    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    
    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }
    
    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }
    
    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    // Validation methods
    public void validate() {

        if (minAge != null && minAge < 0) {
            throw new IllegalArgumentException("minAge cannot be negative");
        }
        if (maxAge != null && maxAge < 0) {
            throw new IllegalArgumentException("maxAge cannot be negative");
        }
        if (minAge != null && maxAge != null && minAge > maxAge) {
            throw new IllegalArgumentException("minAge cannot be greater than maxAge");
        }
        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException("minPrice cannot be negative");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException("maxPrice cannot be negative");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        if (type != null && !isValidType(type)) {
            throw new IllegalArgumentException("type must be one of: ONE_TIME, COURSE, CLUB");
        }
        if (sort != null && !isValidSort(sort)) {
            throw new IllegalArgumentException("sort must be one of: upcoming, priceAsc, priceDesc");
        }
    }
    
    private boolean isValidType(String type) {
        
        return "ONE_TIME".equals(type) || "COURSE".equals(type) || "CLUB".equals(type);
    }
    
    private boolean isValidSort(String sort) {

        return "upcoming".equals(sort) || "priceAsc".equals(sort) || "priceDesc".equals(sort);

    }
}