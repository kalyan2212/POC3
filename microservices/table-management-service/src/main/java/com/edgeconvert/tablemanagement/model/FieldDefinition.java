package com.edgeconvert.tablemanagement.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class FieldDefinition {
    private String fieldId;
    private String name;
    private String dataType;
    private Integer varcharLength;
    private Boolean isPrimaryKey;
    private Boolean disallowNull;
    private String defaultValue;
    private Integer order;
    
    public FieldDefinition() {}
    
    public FieldDefinition(String fieldId, String name, String dataType) {
        this.fieldId = fieldId;
        this.name = name;
        this.dataType = dataType;
        this.isPrimaryKey = false;
        this.disallowNull = false;
        this.defaultValue = "";
        this.varcharLength = 255;
        this.order = 0;
    }
    
    public String getFieldId() { return fieldId; }
    public void setFieldId(String fieldId) { this.fieldId = fieldId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    
    public Integer getVarcharLength() { return varcharLength; }
    public void setVarcharLength(Integer varcharLength) { this.varcharLength = varcharLength; }
    
    public Boolean getIsPrimaryKey() { return isPrimaryKey; }
    public void setIsPrimaryKey(Boolean isPrimaryKey) { this.isPrimaryKey = isPrimaryKey; }
    
    public Boolean getDisallowNull() { return disallowNull; }
    public void setDisallowNull(Boolean disallowNull) { this.disallowNull = disallowNull; }
    
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    
    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }
}