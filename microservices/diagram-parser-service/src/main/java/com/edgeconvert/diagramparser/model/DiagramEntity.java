package com.edgeconvert.diagramparser.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DiagramEntity {
    private String id;
    private String name;
    private String content;
    private String status;
    private String userId;
    private Long createdAt;
    private Long updatedAt;
    
    public DiagramEntity() {}
    
    public DiagramEntity(String id, String name, String content, String userId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.userId = userId;
        this.status = "UPLOADED";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}