package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSOrderAttribute {
    private String uuid;
    private String display;
    private Object value;  // Changed to Object to handle both String and Boolean
    private OpenMRSOrderAttributeType attributeType;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public OpenMRSOrderAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(OpenMRSOrderAttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public boolean isAttributeType(String attributeName) {
        return attributeType != null && 
               attributeType.getDisplay() != null && 
               attributeType.getDisplay().equalsIgnoreCase(attributeName);
    }

    public boolean hasValueTrue() {
        if (value == null) {
            return false;
        }
        // Handle both Boolean and String types
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return ((String) value).equalsIgnoreCase("true");
        }
        return false;
    }
}
