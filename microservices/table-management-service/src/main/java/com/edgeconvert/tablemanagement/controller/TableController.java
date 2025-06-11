package com.edgeconvert.tablemanagement.controller;

import com.edgeconvert.tablemanagement.model.TableDefinition;
import com.edgeconvert.tablemanagement.model.FieldDefinition;
import com.edgeconvert.tablemanagement.service.TableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "*")
public class TableController {
    
    @Autowired
    private TableManagementService tableService;
    
    @PostMapping
    public ResponseEntity<TableDefinition> createTable(@RequestBody CreateTableRequest request) {
        try {
            String tableId = UUID.randomUUID().toString();
            TableDefinition table = new TableDefinition(
                request.getDiagramId(), 
                tableId, 
                request.getName(), 
                request.getUserId()
            );
            table.setFields(request.getFields());
            
            TableDefinition savedTable = tableService.saveTable(table);
            return ResponseEntity.ok(savedTable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/diagram/{diagramId}")
    public ResponseEntity<List<TableDefinition>> getTablesByDiagram(@PathVariable String diagramId) {
        List<TableDefinition> tables = tableService.getTablesByDiagram(diagramId);
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/{diagramId}/{tableId}")
    public ResponseEntity<TableDefinition> getTable(
            @PathVariable String diagramId, 
            @PathVariable String tableId) {
        TableDefinition table = tableService.getTable(diagramId, tableId);
        if (table == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(table);
    }
    
    @PutMapping("/{diagramId}/{tableId}")
    public ResponseEntity<TableDefinition> updateTable(
            @PathVariable String diagramId,
            @PathVariable String tableId,
            @RequestBody UpdateTableRequest request) {
        try {
            TableDefinition table = tableService.getTable(diagramId, tableId);
            if (table == null) {
                return ResponseEntity.notFound().build();
            }
            
            if (request.getName() != null) {
                table.setName(request.getName());
            }
            if (request.getFields() != null) {
                table.setFields(request.getFields());
            }
            table.setUpdatedAt(System.currentTimeMillis());
            
            TableDefinition updatedTable = tableService.saveTable(table);
            return ResponseEntity.ok(updatedTable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{diagramId}/{tableId}")
    public ResponseEntity<Void> deleteTable(
            @PathVariable String diagramId, 
            @PathVariable String tableId) {
        try {
            tableService.deleteTable(diagramId, tableId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Request classes
    public static class CreateTableRequest {
        private String diagramId;
        private String name;
        private String userId;
        private List<FieldDefinition> fields;
        
        public String getDiagramId() { return diagramId; }
        public void setDiagramId(String diagramId) { this.diagramId = diagramId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public List<FieldDefinition> getFields() { return fields; }
        public void setFields(List<FieldDefinition> fields) { this.fields = fields; }
    }
    
    public static class UpdateTableRequest {
        private String name;
        private List<FieldDefinition> fields;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public List<FieldDefinition> getFields() { return fields; }
        public void setFields(List<FieldDefinition> fields) { this.fields = fields; }
    }
}