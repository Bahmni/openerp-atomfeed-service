package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSConcept {
    private String uuid;
    private String name;
    private String shortName;
    private String units;
    private String dataType;
    private String conceptClass;
    private boolean set;

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getUnits() {
        return units;
    }

    public String getDataType() {
        return dataType;
    }

    public String getConceptClass() {
        return conceptClass;
    }

    public boolean isSet() {
        return set;
    }
}
