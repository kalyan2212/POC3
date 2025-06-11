package com.edgeconvert.tablemanagement.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
public class TableDefinition {
    private String diagramId;
    private String tableId;
    private String name;
    private List<FieldDefinition> fields;
    private String userId;
    private Long createdAt;
    private Long updatedAt;
    
    public TableDefinition() {}
    
    public TableDefinition(String diagramId, String tableId, String name, String userId) {
        this.diagramId = diagramId;
        this.tableId = tableId;
        this.name = name;
        this.userId = userId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @DynamoDbPartitionKey
    public String getDiagramId() { return diagramId; }
    public void setDiagramId(String diagramId) { this.diagramId = diagramId; }
    
    @DynamoDbSortKey
    public String getTableId() { return tableId; }
    public void setTableId(String tableId) { this.tableId = tableId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<FieldDefinition> getFields() { return fields; }
    public void setFields(List<FieldDefinition> fields) { this.fields = fields; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
}