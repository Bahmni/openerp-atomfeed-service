package org.bahmni.feed.openerp.domain.encounter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSConcept {
    private String uuid;
    private OpenMRSConceptName name;
    private boolean set;

    public String getUuid() {
        return uuid;
    }

    public OpenMRSConceptName getName() {
        return name;
    }

    public boolean isSet() {
        return set;
    }
}
