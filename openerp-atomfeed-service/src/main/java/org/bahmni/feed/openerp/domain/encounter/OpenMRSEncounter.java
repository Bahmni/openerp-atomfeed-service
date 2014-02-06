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
import org.bahmni.feed.openerp.domain.OpenMRSPatient;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSEncounter extends OpenMRSEncounterEvent {
    private OpenMRSPatient patient;
    private OpenMRSEncounterType encounterType;
    private String uuid;
    private List<OpenMRSOrder> orders = new ArrayList<>();
    private OpenMRSVisit visit;

    public static final String ADMISSION_CHARGES = "Admission Charges";

    public static final String TYPE_ADMISSION = "ADMISSION";
    public static final String TYPE_DISCHARGE = "DISCHARGE";
    public static final String TYPE_TRANSFER = "TRANSFER";
    // These need to match the OpenMRS ADT Concept set members.
    private static final String[] ADT_EVENTS_ARRAY = new String[]{TYPE_ADMISSION, TYPE_DISCHARGE, TYPE_TRANSFER};

    public List<Parameter> getParameters(String eventId, ProductService productService, String feedURIForLastReadEntry, String feedURI) throws IOException {
        List<Parameter> parameters = new ArrayList<>();
        String patientDisplay = getPatient().getDisplay();
        String patientId = patientDisplay.split(" ")[0];
        validateUrls(feedURIForLastReadEntry, feedURI);

        parameters.add(createParameter("category", "create.sale.order", "string"));
        parameters.add(createParameter("customer_id", patientId, "string"));
        parameters.add(createParameter("feed_uri", feedURI, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));

        OpenERPOrders orders = new OpenERPOrders();
        orders.setId(getUuid());

        mapOrders(parameters, orders, productService);
        return parameters;
    }

    public boolean shouldERPConsumeEvent() {
        return isADTEvent() || hasOrders();
    }

    private void mapOrders(List<Parameter> parameters, OpenERPOrders orders, ProductService productService) throws IOException {
        if (hasOrders()) {
            for (OpenMRSOrder order : getOrders()) {
                addNewOrder(orders, visit, order);
            }
        } else if (isAdmissionEncounter()) {
            addAdmissionChargeOrder(orders, visit, productService);
        }
        String ordersJson = ObjectMapperRepository.objectMapper.writeValueAsString(orders);
        parameters.add(createParameter("orders", ordersJson, "string"));
    }

    private boolean isAdmissionEncounter() {
        return getEncounterType().getName().equals(OpenMRSEncounter.TYPE_ADMISSION);
    }

    private void addAdmissionChargeOrder(OpenERPOrders orders, OpenMRSVisit visit, ProductService productService) {
        String productId = productService.findProductByName(ADMISSION_CHARGES);
        if (productId == null)
            throw new OpenERPException("Product " + ADMISSION_CHARGES + " not Found");

        List<String> productIds = new ArrayList<>();
        productIds.add(productId);

        OpenERPOrder openERPOrder = new OpenERPOrder();
        openERPOrder.setVisitId(visit.getUuid());
        openERPOrder.setVisitType(visit.getVisitType());
        openERPOrder.setDescription(visit.getDescription());
        openERPOrder.setProductIds(productIds);

        orders.getOpenERPOrders().add(openERPOrder);
    }

    private void addNewOrder(OpenERPOrders orders, OpenMRSVisit visit, OpenMRSOrder order) {
        OpenERPOrder openERPOrder = new OpenERPOrder();
        openERPOrder.setId(order.getUuid());
        openERPOrder.setVoided(order.isVoided());
        openERPOrder.setVisitId(visit.getUuid());
        openERPOrder.setVisitType(visit.getVisitType());
        openERPOrder.setDescription(visit.getDescription());

        List<String> productIds = new ArrayList<>();
        productIds.add(order.getConcept().getUuid());
        openERPOrder.setProductIds(productIds);
        orders.getOpenERPOrders().add(openERPOrder);
    }


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
