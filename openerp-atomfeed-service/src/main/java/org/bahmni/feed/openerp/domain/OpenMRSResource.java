package org.bahmni.feed.openerp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSResource {

    private String uuid;
    private Date dateCreated;
    private Date lastUpdated;
    private String name;
    private Boolean active;

    private Map<String, String> properties;

    public String getUuid() {
        return uuid;
    }

    @JsonProperty("id")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    @JsonProperty("isActive")
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
