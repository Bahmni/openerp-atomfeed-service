package org.bahmni.feed.openerp.domain.encounter;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.feed.openerp.domain.visit.VisitAttributes;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapERPOrders extends OpenMRSEncounterEvent {

    private final Boolean isOdoo16;
    private OpenMRSEncounter openMRSEncounter;
    private OpenMRSVisit openMRSVisit;
    private OpenMRSWebClient openMRSWebClient;
    private OpenERPAtomFeedProperties openERPAtomFeedProperties;
    private static Logger logger = LoggerFactory.getLogger(MapERPOrders.class);

    public MapERPOrders(OpenMRSEncounter openMRSEncounter, OpenMRSVisit openMRSVisit, OpenMRSWebClient openMRSWebClient, OpenERPAtomFeedProperties openERPAtomFeedProperties, Boolean isOdoo16) {
        this.openMRSEncounter = openMRSEncounter;
        this.openMRSVisit = openMRSVisit;
        this.openMRSWebClient = openMRSWebClient;
        this.openERPAtomFeedProperties = openERPAtomFeedProperties;
        this.isOdoo16 = isOdoo16;
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
        List<Provider> providers = openMRSEncounter.getProviders();

        List<OpenMRSObservation> observations = openMRSEncounter.getObservations();
        String providerName = providers.size() != 0 ? providers.get(0).getName() : "";
        for (OpenMRSDrugOrder drugOrder : openMRSEncounter.getDrugOrders()) {
            if(drugOrder.getDrugNonCoded() != null) {
                continue;
            }
            OpenERPOrder openERPOrder = new OpenERPOrder();
            openERPOrder.setVisitId(openMRSEncounter.getVisitUuid());
            openERPOrder.setOrderId(drugOrder.getUuid());
            openERPOrder.setDispensed("false");
            observations.stream().filter(o -> o.getOrderUuid() != null && o.getOrderUuid().equals(openERPOrder.getOrderId()))
                    .forEach(o -> {
                        if ((o.getConcept()  != null)
                                && o.getConcept().getName().equalsIgnoreCase("Dispensed")) {
                            if (Boolean.parseBoolean(o.getValue().toString())) {
                                openERPOrder.setDispensed("true");
                            }
                        }
                    });
            openERPOrder.setPreviousOrderId(drugOrder.getPreviousOrderUuid());
            openERPOrder.setEncounterId(openMRSEncounter.getEncounterUuid());
            openERPOrder.setProductId(drugOrder.getDrugUuid());
            openERPOrder.setProductName(drugOrder.getDrugName());
            openERPOrder.setAction(drugOrder.getAction());
            openERPOrder.setDateCreated(drugOrder.getDateActivated());
            openERPOrder.setQuantity(drugOrder.getQuantity());
            openERPOrder.setQuantityUnits(drugOrder.getQuantityUnits());
            openERPOrder.setVoided(drugOrder.isVoided());
            openERPOrder.setType(drugOrder.getOrderType());
            openERPOrder.setVisitType(getVisitType());
            openERPOrder.setProviderName(providerName);
            openERPOrder.setConceptName(drugOrder.getConceptName());
            openERPOrders.add(openERPOrder);

            if(isOdoo16) {
                addPreviousDrugOrder(openERPOrder, openERPOrders);
            }
        }

        Map<String, OpenERPOrder> latestOrders = new LinkedHashMap<>();
        for (OpenMRSOrder order : openMRSEncounter.getOrders()) {
            OpenERPOrder openERPOrder = new OpenERPOrder();
            openERPOrder.setVisitId(openMRSEncounter.getVisitUuid());
            openERPOrder.setOrderId(order.getUuid());
            openERPOrder.setDispensed("false");
            openERPOrder.setPreviousOrderId(order.getPreviousOrderUuid());
            openERPOrder.setEncounterId(openMRSEncounter.getEncounterUuid());
            openERPOrder.setProductId(order.getConceptUuid());
            openERPOrder.setProductName(order.getConceptName());
            openERPOrder.setAction(order.getAction());
            openERPOrder.setDateCreated(order.getDateCreated());
            openERPOrder.setQuantity((double) 1);
            openERPOrder.setQuantityUnits("Unit(s)");
            openERPOrder.setVoided(order.isVoided());
            openERPOrder.setType(order.getOrderType());
            openERPOrder.setVisitType(getVisitType());
            openERPOrder.setProviderName(providerName);

            latestOrders = findLatestOrder(openERPOrder, latestOrders);
        }
        for (OpenERPOrder latestOrder : latestOrders.values()) {
            openERPOrders.add(latestOrder);
        }
        return ObjectMapperRepository.objectMapper.writeValueAsString(openERPOrders);
    }

    private void addPreviousDrugOrder(OpenERPOrder openERPOrder, OpenERPOrders openERPOrders) {
        if ("REVISE".equals(openERPOrder.getAction()) && !isPreviousOrderFound(openERPOrder, openERPOrders)) {
            OpenERPOrder previousOrder = fetchPreviousDrugOrder(openERPOrder.getPreviousOrderId());
            if (previousOrder != null) {
                openERPOrders.add(previousOrder);
            }
        }
    }

    private boolean isPreviousOrderFound(OpenERPOrder openERPOrder, OpenERPOrders openERPOrders) {
        List<OpenERPOrder> existingOrders = openERPOrders.getOpenERPOrders();
        for (OpenERPOrder existingOrder : existingOrders) {
            if (existingOrder.getOrderId().equals(openERPOrder.getPreviousOrderId())) {
                return true;
            }
        }
        return false;
    }

    private OpenERPOrder fetchPreviousDrugOrder(String previousOrderId) {
        String apiUrl = openERPAtomFeedProperties.getDrugOrderFeedUri() + "/" + previousOrderId;
        final ObjectMapper mapper = new ObjectMapper();
        try {
            String response = openMRSWebClient.get(URI.create(apiUrl));
            OpenMRSDrugOrder previousOrder = mapper.readValue(response, OpenMRSDrugOrder.class);
            OpenERPOrder order = new OpenERPOrder();
            order.setOrderId(previousOrder.getUuid());
            order.setPreviousOrderId(previousOrder.getUuid());
            order.setEncounterId(previousOrder.getEncounterUuid());
            order.setProductId(previousOrder.getDrugUuid());
            order.setProductName(previousOrder.getDrugName());
            order.setQuantity(previousOrder.getQuantity());
            order.setQuantityUnits(previousOrder.getQuantityUnits());
            order.setAction(previousOrder.getAction());
            order.setVisitId(previousOrder.getVisitUuid());
            order.setVisitType(getVisitType());
            order.setType(previousOrder.getOrderType());
            order.setVoided(previousOrder.isVoided());
            order.setProviderName(previousOrder.getProviderName());
            order.setDispensed("false");
            order.setConceptName(previousOrder.getConceptName());
            order.setDateCreated(previousOrder.getDateActivated());
            return order;
        } catch (Exception e) {
            logger.error("Error occurred while fetching previous drug order: " + e.getMessage());
            throw new RuntimeException("Failed to fetch previous drug order", e);
        }
    }

    //Filters orders to keep only the latest action for each product. This is necessary for ensuring consistent and accurate quotation generation, particularly when order objects may not be in chronological order.
    private Map<String, OpenERPOrder> findLatestOrder(OpenERPOrder openERPOrder, Map<String, OpenERPOrder> latestOrders) {
        String productId = openERPOrder.getProductId();
        if (!latestOrders.containsKey(productId) || latestOrders.get(productId).getDateCreated().before(openERPOrder.getDateCreated())) {
            latestOrders.put(productId, openERPOrder);
        }
        return latestOrders;
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
