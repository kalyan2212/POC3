package com.edgeconvert.diagramparser.repository;

import com.edgeconvert.diagramparser.model.DiagramEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DiagramRepository {
    
    private final DynamoDbTable<DiagramEntity> diagramTable;
    
    @Autowired
    public DiagramRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.diagramTable = dynamoDbEnhancedClient.table("diagrams", TableSchema.fromBean(DiagramEntity.class));
    }
    
    public DiagramEntity save(DiagramEntity diagram) {
        diagramTable.putItem(diagram);
        return diagram;
    }
    
    public DiagramEntity findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return diagramTable.getItem(key);
    }
    
    public List<DiagramEntity> findByUserId(String userId) {
        // Note: This would require a GSI on userId in a real implementation
        // For now, returning empty list - would need proper GSI setup
        return List.of();
    }
    
    public void deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        diagramTable.deleteItem(key);
    }
}