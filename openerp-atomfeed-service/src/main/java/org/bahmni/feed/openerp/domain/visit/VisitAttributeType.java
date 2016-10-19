package org.bahmni.feed.openerp.domain.visit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitAttributeType {

    private String display;

    public String getDisplay() {
        return display;
    }
}
