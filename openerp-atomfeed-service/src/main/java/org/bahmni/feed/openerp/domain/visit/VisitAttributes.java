package org.bahmni.feed.openerp.domain.visit;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitAttributes {


    private VisitAttributeType attributeType;

    public String getValue() {
        return value;
    }

    private String value;

    public VisitAttributeType getAttributeType() {
        return attributeType;
    }
}
