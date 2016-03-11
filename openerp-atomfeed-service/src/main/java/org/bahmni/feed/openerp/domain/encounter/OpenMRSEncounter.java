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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSEncounter {
    private List<OpenMRSDrugOrder> drugOrders = new ArrayList<>();
    private List<OpenMRSOrder> orders = new ArrayList<>();
    private List<OpenMRSObservation> observations = new ArrayList<>();
    private List<Provider> providers = new ArrayList<>();
    private String patientUuid;
    private String patientId;
    private String encounterUuid;
    private String visitUuid;
    private String visitTypeUuid;
    private String locationName;

    public boolean shouldERPConsumeEvent() {
        return hasDrugOrders() || hasOrders();
    }

    public String getEncounterUuid() {
        return encounterUuid;
    }

    private boolean hasDrugOrders() {
        return getDrugOrders().size() > 0;
    }

    private boolean hasOrders() {
        return getOrders().size() > 0;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public String getPatientId() {
        return patientId;
    }

    public List<OpenMRSDrugOrder> getDrugOrders() {
        return drugOrders;
    }

    public List<OpenMRSOrder> getOrders() {
        return orders;
    }

    public List<OpenMRSObservation> getObservations() {
        return observations;
    }

    public void setObservations(List<OpenMRSObservation> observations) {
        this.observations = observations;
    }

    public String getVisitUuid() {
        return visitUuid;
    }

    public String getVisitTypeUuid() {
        return visitTypeUuid;
    }

    public String getLocationName() { return locationName; }

    public List<Provider> getProviders() {
        return providers;
    }
}
