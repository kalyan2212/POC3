package com.edgeconvert.diagramparser.controller;

import com.edgeconvert.diagramparser.service.DiagramService;
import com.edgeconvert.diagramparser.service.EdgeFileParserService;
import com.edgeconvert.diagramparser.model.DiagramEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diagrams")
@CrossOrigin(origins = "*")
public class DiagramController {
    
    @Autowired
    private DiagramService diagramService;
    
    @Autowired
    private EdgeFileParserService parserService;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDiagram(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        try {
            String content = new String(file.getBytes());
            String diagramId = UUID.randomUUID().toString();
            
            DiagramEntity diagram = new DiagramEntity(diagramId, file.getOriginalFilename(), content, userId);
            diagramService.saveDiagram(diagram);
            
            return ResponseEntity.ok().body(new UploadResponse(diagramId, "File uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to upload file: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{diagramId}/parse")
    public ResponseEntity<?> parseDiagram(@PathVariable String diagramId) {
        try {
            DiagramEntity diagram = diagramService.getDiagram(diagramId);
            if (diagram == null) {
                return ResponseEntity.notFound().build();
            }
            
            EdgeFileParserService.ParsedDiagram parsed = parserService.parseEdgeFile(diagram.getContent());
            
            diagram.setStatus("PARSED");
            diagram.setUpdatedAt(System.currentTimeMillis());
            diagramService.saveDiagram(diagram);
            
            return ResponseEntity.ok(parsed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to parse diagram: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{diagramId}")
    public ResponseEntity<DiagramEntity> getDiagram(@PathVariable String diagramId) {
        DiagramEntity diagram = diagramService.getDiagram(diagramId);
        if (diagram == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(diagram);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DiagramEntity>> getUserDiagrams(@PathVariable String userId) {
        List<DiagramEntity> diagrams = diagramService.getDiagramsByUser(userId);
        return ResponseEntity.ok(diagrams);
    }
    
    @DeleteMapping("/{diagramId}")
    public ResponseEntity<Void> deleteDiagram(@PathVariable String diagramId) {
        try {
            diagramService.deleteDiagram(diagramId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Response classes
    public static class UploadResponse {
        private String diagramId;
        private String message;
        
        public UploadResponse(String diagramId, String message) {
            this.diagramId = diagramId;
            this.message = message;
        }
        
        public String getDiagramId() { return diagramId; }
        public String getMessage() { return message; }
    }
    
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() { return error; }
    }
}