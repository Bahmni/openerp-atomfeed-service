package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.extension.SaleOrderParameterExtension;
import org.bahmni.odooconnect.extensions.SaleOrderParameterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.encounter.MapERPOrders;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.springframework.context.ApplicationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class OpenERPSaleOrderEventWorker implements EventWorker {
    private final Boolean isOdoo16;
    private final ApplicationContext applicationContext;
    OpenERPContext openERPContext;
    private final String feedUrl;
    private final String odooURL;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;
    private final OpenERPAtomFeedProperties openERPAtomFeedProperties;


    private static Logger logger = LoggerFactory.getLogger(OpenERPSaleOrderEventWorker.class);

    public OpenERPSaleOrderEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenMRSWebClient webClient, String urlPrefix, OpenERPAtomFeedProperties openERPAtomFeedProperties, Boolean isOdoo16, ApplicationContext applicationContext) {
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.openERPContext = openERPContext;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        this.openERPAtomFeedProperties = openERPAtomFeedProperties;
        this.isOdoo16 = isOdoo16;
        this.applicationContext = applicationContext;
    }

    @Override
    public void process(Event event) {
        try {
            OpenERPRequest openERPRequest = mapRequest(event);
            if (!openERPRequest.shouldERPConsumeEvent())
                return;
            if (isOpenERPOrdersEmpty(openERPRequest)) {
                logger.info("Skipping event processing as openERPOrders is empty");
                return;
            }
            openERPContext.execute(openERPRequest, odooURL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {

        String encounterEventContent = webClient.get(URI.create(urlPrefix + event.getContent()));
        OpenMRSEncounter openMRSEncounter = ObjectMapperRepository.objectMapper.readValue(encounterEventContent, OpenMRSEncounter.class);

        // Ignore Bed Assignment Encounter events
        if(!openMRSEncounter.shouldERPConsumeEvent())
            return OpenERPRequest.DO_NOT_CONSUME;

        String visitURL = "/openmrs/ws/rest/v1/visit/" + openMRSEncounter.getVisitUuid() + "?v=full";
        String visitContent = webClient.get(URI.create(urlPrefix + visitURL));

        OpenMRSVisit openMRSVisit = ObjectMapperRepository.objectMapper.readValue(visitContent, OpenMRSVisit.class);
        MapERPOrders mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit, webClient, openERPAtomFeedProperties, isOdoo16);

        OpenERPRequest erpRequest = new OpenERPRequest("atom.event.worker", "process_event", mapERPOrders.getParameters(event.getId(), event.getFeedUri(), feedUrl));
        if (event.getFeedUri() == null)
            erpRequest.addParameter(createParameter("is_failed_event", "1", "boolean"));

        List<Parameter> extensionParams = getExtensionParams(openMRSEncounter);
        if (!extensionParams.isEmpty()) {
            for (Parameter parameter : extensionParams) {
                erpRequest.addParameter(parameter);
            }
        }
        return erpRequest;
    }

    private List<Parameter> getExtensionParams(OpenMRSEncounter openMRSEncounter) {
        if (applicationContext == null) {
            return java.util.Collections.emptyList();
        }
        List<SaleOrderParameterProvider> providers = new java.util.ArrayList<>(
                applicationContext.getBeansOfType(SaleOrderParameterProvider.class).values()
        );
        logger.debug("Found {} SaleOrderParameter providers", providers.size());
        if (providers.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return SaleOrderParameterExtension.getExtensionParameters(openMRSEncounter, providers, webClient, urlPrefix);
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

    private boolean isOpenERPOrdersEmpty(OpenERPRequest openERPRequest) {
        try {
            for (Parameter parameter : openERPRequest.getParameters()) {
                if ("orders".equals(parameter.getName())) {
                    String ordersJson = parameter.getValue();
                    JsonNode ordersNode = ObjectMapperRepository.objectMapper.readTree(ordersJson);
                    JsonNode openERPOrders = ordersNode.get("openERPOrders");
                    return openERPOrders != null && openERPOrders.isArray() && openERPOrders.size() == 0;
                }
            }
        } catch (Exception e) {
            logger.error("Error checking if openERPOrders is empty", e);
        }
        return false;
    }
}
