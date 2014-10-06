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

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSEncounter extends OpenMRSEncounterEvent {
    private List<OpenMRSDrugOrder> drugOrders = new ArrayList<>();
    private String patientUuid;
    private String patientId;
    private String encounterUuid;
    private String visitUuid;


    public List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI) throws IOException {
        List<Parameter> parameters = new ArrayList<>();
        validateUrls(feedURIForLastReadEntry, feedURI);

        parameters.add(createParameter("category", "create.sale.order", "string"));
        parameters.add(createParameter("customer_id", getPatientId(), "string"));
        parameters.add(createParameter("encounter_id", getEncounterUuid(), "string"));
        parameters.add(createParameter("feed_uri", feedURI, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        parameters.add(createParameter("orders", mapOpenERPOrders(), "string"));
        return parameters;
    }

    private String mapOpenERPOrders() throws IOException {
        OpenERPOrders openERPOrders = new OpenERPOrders(getEncounterUuid());
        for (OpenMRSDrugOrder drugOrder : getDrugOrders()) {
            OpenERPOrder openERPOrder = new OpenERPOrder();
            openERPOrder.setOrderId(drugOrder.getUuid());
            openERPOrder.setPreviousOrderId(drugOrder.getPreviousOrderUuid());
            openERPOrder.setEncounterId(getEncounterUuid());
            openERPOrder.setProductId(drugOrder.getDrugUuid());
            openERPOrder.setProductName(drugOrder.getDrugName());
            openERPOrder.setAction(drugOrder.getAction());
            openERPOrder.setQuantity(drugOrder.getQuantity());
            openERPOrder.setQuantityUnits(drugOrder.getQuantityUnits());
            openERPOrder.setVoided(drugOrder.isVoided());

            openERPOrders.add(openERPOrder);
        }

        return ObjectMapperRepository.objectMapper.writeValueAsString(openERPOrders);
    }

    public boolean shouldERPConsumeEvent() {
        return hasDrugOrders();
    }

    public String getEncounterUuid() {
        return encounterUuid;
    }

    private boolean hasDrugOrders() {
        return getDrugOrders().size() > 0;
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

    public String getVisitUuid() {
        return visitUuid;
    }
}
