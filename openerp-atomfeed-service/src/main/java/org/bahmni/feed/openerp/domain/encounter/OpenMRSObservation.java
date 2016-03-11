package org.bahmni.feed.openerp.domain.encounter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSObservation {
    private String orderUuid;
    private String conceptNameToDisplay;
    private String value;

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getConceptNameToDisplay() {
        return conceptNameToDisplay;
    }

    public void setConceptNameToDisplay(String conceptNameToDisplay) {
        this.conceptNameToDisplay = conceptNameToDisplay;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
