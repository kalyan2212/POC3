package com.edgeconvert.tablemanagement.service;

import com.edgeconvert.tablemanagement.model.TableDefinition;
import com.edgeconvert.tablemanagement.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableManagementService {
    
    @Autowired
    private TableRepository tableRepository;
    
    public TableDefinition saveTable(TableDefinition table) {
        return tableRepository.save(table);
    }
    
    public TableDefinition getTable(String diagramId, String tableId) {
        return tableRepository.findById(diagramId, tableId);
    }
    
    public List<TableDefinition> getTablesByDiagram(String diagramId) {
        return tableRepository.findByDiagramId(diagramId);
    }
    
    public void deleteTable(String diagramId, String tableId) {
        tableRepository.deleteById(diagramId, tableId);
    }
}