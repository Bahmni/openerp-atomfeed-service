/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package org.bahmni.feed.openerp.domain.encounter;

import org.bahmni.feed.openerp.domain.OpenMRSPatient;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSEncounter {
    private OpenMRSPatient patient;
    private OpenMRSEncounterType encounterType;
    private String uuid;
    private List<OpenMRSOrder> orders = new ArrayList<>();
    private OpenMRSVisit visit;

    public static final String TYPE_ADMISSION = "ADMISSION";
    public static final String TYPE_DISCHARGE = "DISCHARGE";
    public static final String TYPE_TRANSFER = "TRANSFER";
    // These need to match the OpenMRS ADT Concept set members.
    private static final String[] ADT_EVENTS_ARRAY = new String[] {TYPE_ADMISSION, TYPE_DISCHARGE, TYPE_TRANSFER};

    public String getUuid() {
        return uuid;
    }

    public List<OpenMRSOrder> getOrders() {
        return orders;
    }

    public OpenMRSPatient getPatient() {
        return patient;
    }

    public OpenMRSVisit getVisit() {
        return visit;
    }

    public boolean shouldERPConsumeEvent() {
        return isADTEvent() || hasOrders();
    }

    private boolean hasOrders() {
        return getOrders().size() > 0;
    }

    public OpenMRSEncounterType getEncounterType() {
        return encounterType;
    }

    private boolean isADTEvent() {
        if (encounterType == null) return false;

        List<String> adtEncounterTypes = Arrays.asList(ADT_EVENTS_ARRAY);
        for (String adtEncounterType : adtEncounterTypes) {
            if (adtEncounterType.equalsIgnoreCase(encounterType.getName()))
                return true;
        }
        return false;
    }
}
