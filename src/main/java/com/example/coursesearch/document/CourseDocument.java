package com.example.coursesearch.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.time.Instant;

@Document(indexName = "courses")
public class CourseDocument {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;
    
    @Field(type = FieldType.Keyword)
    private String category;
    
    @Field(type = FieldType.Keyword)
    private CourseType type;
    
    @Field(type = FieldType.Keyword)
    private String gradeRange;
    
    @Field(type = FieldType.Integer)
    private Integer minAge;
    
    @Field(type = FieldType.Integer)
    private Integer maxAge;
    
    @Field(type = FieldType.Double)
    private Double price;
    
    @Field(type = FieldType.Date)
    @JsonProperty("nextSessionDate")
    private Instant nextSessionDate;
    
    // Completion field for autocomplete suggestions
    @CompletionField(maxInputLength = 100)
    private Completion suggest;
    
    // Constructors
    public CourseDocument() {}
    
    public CourseDocument(String id, String title, String description, String category, 
                         CourseType type, String gradeRange, Integer minAge, Integer maxAge, 
                         Double price, Instant nextSessionDate)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.gradeRange = gradeRange;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.price = price;
        this.nextSessionDate = nextSessionDate;
        this.suggest = new Completion(new String[]{title});
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public CourseType getType() { return type; }
    public void setType(CourseType type) { this.type = type; }
    
    public String getGradeRange() { return gradeRange; }
    public void setGradeRange(String gradeRange) { this.gradeRange = gradeRange; }
    
    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }
    
    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Instant getNextSessionDate() { return nextSessionDate; }
    public void setNextSessionDate(Instant nextSessionDate) { this.nextSessionDate = nextSessionDate; }
    
    public Completion getSuggest() { return suggest; }
    public void setSuggest(Completion suggest) { this.suggest = suggest; }

    
    public enum CourseType {
        ONE_TIME, COURSE, CLUB
    }
}