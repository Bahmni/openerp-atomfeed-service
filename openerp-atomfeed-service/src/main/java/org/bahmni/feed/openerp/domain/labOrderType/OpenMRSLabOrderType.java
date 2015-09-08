package org.bahmni.feed.openerp.domain.labOrderType;

import org.codehaus.jackson.annotate.JsonProperty;

public abstract class OpenMRSLabOrderType {

    private String uuid;
    private String name;

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
}
