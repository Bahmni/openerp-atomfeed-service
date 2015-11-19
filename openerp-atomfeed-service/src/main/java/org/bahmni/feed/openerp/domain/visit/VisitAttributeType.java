package org.bahmni.feed.openerp.domain.visit;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitAttributeType {

    private String display;

    public String getDisplay() {
        return display;
    }
}
