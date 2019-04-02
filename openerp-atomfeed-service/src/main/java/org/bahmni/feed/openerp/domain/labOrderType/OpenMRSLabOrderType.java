package org.bahmni.feed.openerp.domain.labOrderType;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public abstract class OpenMRSLabOrderType {

    private String uuid;
    private String name;
    private boolean active;

    private Map<String, String> properties;

    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }

    @JsonProperty("id")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active ? "1": "0";
    }

    @JsonProperty("isActive")
    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, String> getProperties() {
        if (properties == null) {
            return new HashMap<>();
        }
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
