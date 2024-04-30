package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
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

import java.io.IOException;
import java.net.URI;

public class OpenERPSaleOrderEventWorker implements EventWorker {
    private final Boolean isOdoo16;
    OpenERPContext openERPContext;
    private final String feedUrl;
    private final String odooURL;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;
    private final OpenERPAtomFeedProperties openERPAtomFeedProperties;


    private static Logger logger = LoggerFactory.getLogger(OpenERPSaleOrderEventWorker.class);

    public OpenERPSaleOrderEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenMRSWebClient webClient, String urlPrefix, OpenERPAtomFeedProperties openERPAtomFeedProperties, Boolean isOdoo16) {
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.openERPContext = openERPContext;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
        this.openERPAtomFeedProperties = openERPAtomFeedProperties;
        this.isOdoo16 = isOdoo16;
    }

    @Override
    public void process(Event event) {
        try {
            OpenERPRequest openERPRequest = mapRequest(event);
            if (!openERPRequest.shouldERPConsumeEvent())
                return;
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

        return erpRequest;
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }
}
