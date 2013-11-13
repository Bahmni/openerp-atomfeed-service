package org.bahmni.feed.openerp.domain;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenMRSEncounterParser;
import org.bahmni.feed.openerp.domain.encounter.OpenERPOrder;
import org.bahmni.feed.openerp.domain.encounter.OpenERPOrders;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSOrder;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenERPRequestParams {

    private Event event;
    private String feedUrl;
    public static ObjectMapper objectMapper = new ObjectMapper();

    public OpenERPRequestParams(Event event, String feedUrl) {
        this.event = event;
        this.feedUrl = feedUrl;
    }

    public List<Parameter> getParameters(String orderJSON) throws IOException {
        OpenMRSEncounterParser openMRSEncounterParser = new OpenMRSEncounterParser(ObjectMapperRepository.objectMapper);
        OpenMRSEncounter openMRSEncounter = openMRSEncounterParser.parse(orderJSON);

        return mapParameters(openMRSEncounter, event.getId(), event.getFeedUri());
    }

    private List<Parameter> mapParameters(OpenMRSEncounter openMRSEncounter, String eventId, String feedUri) throws IOException {
        List<Parameter> parameters = new ArrayList<>();
        String patientDisplay = openMRSEncounter.getPatient().getDisplay();
        String patientId = patientDisplay.split(" ")[0];

        parameters.add(createParameter("category", "create.sale.order", "string"));
        parameters.add(createParameter("customer_id", patientId, "string"));
        if((feedUrl != null && feedUrl.contains("$param.value")) || (feedUri != null && feedUri.contains("$param.value")))
            throw new RuntimeException("Junk values in the feedUrl:$param.value");
        parameters.add(createParameter("feed_uri", feedUrl, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedUri, "string"));

        OpenERPOrders orders = new OpenERPOrders();
        orders.setId(openMRSEncounter.getUuid());

        mapOrders(openMRSEncounter, parameters, orders);
        return parameters;
    }

    private void mapOrders(OpenMRSEncounter openMRSEncounter, List<Parameter> parameters, OpenERPOrders orders) throws IOException {
        OpenMRSVisit visit = openMRSEncounter.getVisit();
        if(openMRSEncounter.getOrders().size() > 0){
            for(OpenMRSOrder order : openMRSEncounter.getOrders())    {
                OpenERPOrder openERPOrder = new OpenERPOrder();
                openERPOrder.setId(order.getUuid());
                setVisitDetails(openERPOrder,visit);
                List<String> productIds = new ArrayList<>();
                productIds.add(order.getConcept().getUuid());
                openERPOrder.setProductIds(productIds);
                orders.getOpenERPOrders().add(openERPOrder);
            }
            String ordersJson = objectMapper.writeValueAsString(orders);

            parameters.add(createParameter("orders", ordersJson, "string"));
        }
    }

    private void setVisitDetails(OpenERPOrder openERPOrder, OpenMRSVisit visit) {
        openERPOrder.setVisitId(visit.getUuid());
        openERPOrder.setVisitType(visit.getVisitType());
        openERPOrder.setDescription(visit.getDescription());
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }
}
