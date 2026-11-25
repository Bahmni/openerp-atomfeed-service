/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSObservation {
    private String orderUuid;
    private String conceptNameToDisplay;
    private Object value;
    private OpenMRSConcept concept;

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public OpenMRSConcept getConcept() {
        return concept;
    }

    public void setConcept(OpenMRSConcept concept) {
        this.concept = concept;
    }
}
