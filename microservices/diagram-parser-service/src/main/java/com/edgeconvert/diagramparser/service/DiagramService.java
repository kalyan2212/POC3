package com.edgeconvert.diagramparser.service;

import com.edgeconvert.diagramparser.model.DiagramEntity;
import com.edgeconvert.diagramparser.repository.DiagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiagramService {
    
    @Autowired
    private DiagramRepository diagramRepository;
    
    public DiagramEntity saveDiagram(DiagramEntity diagram) {
        return diagramRepository.save(diagram);
    }
    
    public DiagramEntity getDiagram(String diagramId) {
        return diagramRepository.findById(diagramId);
    }
    
    public List<DiagramEntity> getDiagramsByUser(String userId) {
        return diagramRepository.findByUserId(userId);
    }
    
    public void deleteDiagram(String diagramId) {
        diagramRepository.deleteById(diagramId);
    }
}