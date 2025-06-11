package com.edgeconvert.tablemanagement.repository;

import com.edgeconvert.tablemanagement.model.TableDefinition;
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
public class TableRepository {
    
    private final DynamoDbTable<TableDefinition> tableDefinitionTable;
    
    @Autowired
    public TableRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.tableDefinitionTable = dynamoDbEnhancedClient.table("tables", TableSchema.fromBean(TableDefinition.class));
    }
    
    public TableDefinition save(TableDefinition table) {
        tableDefinitionTable.putItem(table);
        return table;
    }
    
    public TableDefinition findById(String diagramId, String tableId) {
        Key key = Key.builder()
                .partitionValue(diagramId)
                .sortValue(tableId)
                .build();
        return tableDefinitionTable.getItem(key);
    }
    
    public List<TableDefinition> findByDiagramId(String diagramId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(diagramId).build()
        );
        return tableDefinitionTable.query(queryConditional)
                .items()
                .stream()
                .collect(Collectors.toList());
    }
    
    public void deleteById(String diagramId, String tableId) {
        Key key = Key.builder()
                .partitionValue(diagramId)
                .sortValue(tableId)
                .build();
        tableDefinitionTable.deleteItem(key);
    }
}