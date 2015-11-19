package org.bahmni.feed.openerp.domain.encounter;


import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.feed.openerp.domain.visit.VisitAttributes;
import org.bahmni.openerp.web.request.builder.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapERPOrders extends OpenMRSEncounterEvent {

    private OpenMRSEncounter openMRSEncounter;
    private OpenMRSVisit openMRSVisit;

    public MapERPOrders(OpenMRSEncounter openMRSEncounter, OpenMRSVisit openMRSVisit) {
        this.openMRSEncounter = openMRSEncounter;
        this.openMRSVisit = openMRSVisit;

    }

    public List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI) throws IOException {
        List<Parameter> parameters = new ArrayList<>();
        validateUrls(feedURIForLastReadEntry, feedURI);
        parameters.add(createParameter("category", "create.sale.order", "string"));
        parameters.add(createParameter("customer_id", openMRSEncounter.getPatientId(), "string"));
        parameters.add(createParameter("encounter_id", openMRSEncounter.getEncounterUuid(), "string"));
        parameters.add(createParameter("feed_uri", feedURI, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        parameters.add(createParameter("orders", mapOpenERPOrders(), "string"));
        parameters.add(createParameter("locationName", openMRSEncounter.getLocationName(), "string"));
        return parameters;
    }


    private String mapOpenERPOrders() throws IOException {
        OpenERPOrders openERPOrders = new OpenERPOrders(openMRSEncounter.getEncounterUuid());
        for (OpenMRSDrugOrder drugOrder : openMRSEncounter.getDrugOrders()) {
            OpenERPOrder openERPOrder = new OpenERPOrder();
            openERPOrder.setVisitId(openMRSEncounter.getVisitUuid());
            openERPOrder.setOrderId(drugOrder.getUuid());
            openERPOrder.setPreviousOrderId(drugOrder.getPreviousOrderUuid());
            openERPOrder.setEncounterId(openMRSEncounter.getEncounterUuid());
            openERPOrder.setProductId(drugOrder.getDrugUuid());
            openERPOrder.setProductName(drugOrder.getDrugName());
            openERPOrder.setAction(drugOrder.getAction());
            openERPOrder.setQuantity(drugOrder.getQuantity());
            openERPOrder.setQuantityUnits(drugOrder.getQuantityUnits());
            openERPOrder.setVoided(drugOrder.isVoided());
            openERPOrder.setType(drugOrder.getOrderType());
            openERPOrder.setVisitType(getVisitType());
            openERPOrders.add(openERPOrder);
        }

        for (OpenMRSOrder order : openMRSEncounter.getOrders()) {
            OpenERPOrder openERPOrder = new OpenERPOrder();
            openERPOrder.setVisitId(openMRSEncounter.getVisitUuid());
            openERPOrder.setOrderId(order.getUuid());
            openERPOrder.setPreviousOrderId(order.getPreviousOrderUuid());
            openERPOrder.setEncounterId(openMRSEncounter.getEncounterUuid());
            openERPOrder.setProductId(order.getConceptUuid());
            openERPOrder.setProductName(order.getConceptName());
            openERPOrder.setAction(order.getAction());
            openERPOrder.setQuantity((double) 1);
            openERPOrder.setQuantityUnits("Unit(s)");
            openERPOrder.setVoided(order.isVoided());
            openERPOrder.setType(order.getOrderType());
            openERPOrder.setVisitType(getVisitType());
            openERPOrders.add(openERPOrder);
        }

        return ObjectMapperRepository.objectMapper.writeValueAsString(openERPOrders);
    }

    private String getVisitType() {
        for (VisitAttributes visitAttribute : openMRSVisit.getAttributes()) {
            if (visitAttribute.getAttributeType().getDisplay().equals("Visit Status")) {
                return visitAttribute.getValue();
            }
        }
        return null;
    }


}
