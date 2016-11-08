package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider {
    private String name;
    private String uuid;

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
