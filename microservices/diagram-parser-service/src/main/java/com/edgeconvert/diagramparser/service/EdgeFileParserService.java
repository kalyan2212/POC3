package com.edgeconvert.diagramparser.service;

import com.edgeconvert.diagramparser.model.DiagramEntity;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.springframework.stereotype.Service;

@Service
public class EdgeFileParserService {
    
    private static final String DELIM = "|";
    
    public ParsedDiagram parseEdgeFile(String content) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(content));
        
        List<ParsedTable> tables = new ArrayList<>();
        List<ParsedField> fields = new ArrayList<>();
        List<ParsedConnector> connectors = new ArrayList<>();
        
        String currentLine;
        int numFigure = 0;
        String text = "";
        String style = "";
        boolean isEntity = false;
        boolean isAttribute = false;
        boolean isUnderlined = false;
        
        while ((currentLine = br.readLine()) != null) {
            currentLine = currentLine.trim();
            
            if (currentLine.startsWith("Figure ")) {
                numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                isEntity = false;
                isAttribute = false;
                isUnderlined = false;
                
                currentLine = br.readLine().trim(); // this should be "{"
                currentLine = br.readLine().trim();
                
                if (!currentLine.startsWith("Style")) {
                    continue;
                } else {
                    style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""));
                    if (style.startsWith("Relation")) {
                        continue; // Skip relations for now
                    }
                    
                    if (style.startsWith("Entity")) {
                        isEntity = true;
                    }
                    if (style.startsWith("Attribute")) {
                        isAttribute = true;
                    }
                    
                    if (!(isEntity || isAttribute)) {
                        continue;
                    }
                    
                    currentLine = br.readLine().trim(); // this should be Text
                    text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")).replaceAll(" ", "");
                    
                    if (text.equals("")) {
                        continue;
                    }
                    
                    int escape = text.indexOf("\\\\");
                    if (escape > 0) {
                        text = text.substring(0, escape);
                    }
                    
                    // Advance to end of record, look for whether the text is underlined
                    do {
                        currentLine = br.readLine().trim();
                        if (currentLine.startsWith("TypeUnderl")) {
                            isUnderlined = true;
                        }
                    } while (!currentLine.equals("}"));
                    
                    if (isEntity) {
                        ParsedTable table = new ParsedTable(numFigure, text);
                        tables.add(table);
                    }
                    
                    if (isAttribute) {
                        ParsedField field = new ParsedField(numFigure, text);
                        field.setIsPrimaryKey(isUnderlined);
                        fields.add(field);
                    }
                }
            }
            
            if (currentLine.startsWith("Connector ")) {
                // Parse connectors for relationships
                numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                currentLine = br.readLine().trim(); // "{"
                
                int endPoint1 = 0, endPoint2 = 0;
                String endStyle1 = "", endStyle2 = "";
                
                while (!(currentLine = br.readLine().trim()).equals("}")) {
                    if (currentLine.startsWith("EndPoint1 ")) {
                        endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                    }
                    if (currentLine.startsWith("EndPoint2 ")) {
                        endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                    }
                    if (currentLine.startsWith("EndStyle1 ")) {
                        endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""));
                    }
                    if (currentLine.startsWith("EndStyle2 ")) {
                        endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""));
                    }
                }
                
                ParsedConnector connector = new ParsedConnector(numFigure, endPoint1, endPoint2, endStyle1, endStyle2);
                connectors.add(connector);
            }
        }
        
        return new ParsedDiagram(tables, fields, connectors);
    }
    
    // Inner classes for parsed data
    public static class ParsedDiagram {
        private final List<ParsedTable> tables;
        private final List<ParsedField> fields;
        private final List<ParsedConnector> connectors;
        
        public ParsedDiagram(List<ParsedTable> tables, List<ParsedField> fields, List<ParsedConnector> connectors) {
            this.tables = tables;
            this.fields = fields;
            this.connectors = connectors;
        }
        
        public List<ParsedTable> getTables() { return tables; }
        public List<ParsedField> getFields() { return fields; }
        public List<ParsedConnector> getConnectors() { return connectors; }
    }
    
    public static class ParsedTable {
        private final int numFigure;
        private final String name;
        
        public ParsedTable(int numFigure, String name) {
            this.numFigure = numFigure;
            this.name = name;
        }
        
        public int getNumFigure() { return numFigure; }
        public String getName() { return name; }
    }
    
    public static class ParsedField {
        private final int numFigure;
        private final String name;
        private boolean isPrimaryKey;
        private int dataType = 0; // Default to VARCHAR
        private int varcharValue = 1;
        private boolean disallowNull = false;
        private String defaultValue = "";
        
        public ParsedField(int numFigure, String name) {
            this.numFigure = numFigure;
            this.name = name;
        }
        
        public int getNumFigure() { return numFigure; }
        public String getName() { return name; }
        public boolean getIsPrimaryKey() { return isPrimaryKey; }
        public void setIsPrimaryKey(boolean isPrimaryKey) { this.isPrimaryKey = isPrimaryKey; }
        public int getDataType() { return dataType; }
        public int getVarcharValue() { return varcharValue; }
        public boolean getDisallowNull() { return disallowNull; }
        public String getDefaultValue() { return defaultValue; }
    }
    
    public static class ParsedConnector {
        private final int numConnector;
        private final int endPoint1;
        private final int endPoint2;
        private final String endStyle1;
        private final String endStyle2;
        
        public ParsedConnector(int numConnector, int endPoint1, int endPoint2, String endStyle1, String endStyle2) {
            this.numConnector = numConnector;
            this.endPoint1 = endPoint1;
            this.endPoint2 = endPoint2;
            this.endStyle1 = endStyle1;
            this.endStyle2 = endStyle2;
        }
        
        public int getNumConnector() { return numConnector; }
        public int getEndPoint1() { return endPoint1; }
        public int getEndPoint2() { return endPoint2; }
        public String getEndStyle1() { return endStyle1; }
        public String getEndStyle2() { return endStyle2; }
    }
}