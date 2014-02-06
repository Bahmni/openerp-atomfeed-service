package org.bahmni.feed.openerp.worker;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.WebClientResponseParser;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenMRSBedAssignmentParser;
import org.bahmni.feed.openerp.OpenMRSEncounterParser;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.OpenERPRequestParams;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OpenERPSaleOrderEventWorker implements EventWorker {
    OpenERPClient openERPClient;
    private String feedUrl;
    private OpenMRSWebClient webClient;
    private String urlPrefix;


    private static Logger logger = Logger.getLogger(OpenERPSaleOrderEventWorker.class);

    public OpenERPSaleOrderEventWorker(String feedUrl, OpenERPClient openERPClient, OpenMRSWebClient webClient, String urlPrefix) {
        this.feedUrl = feedUrl;
        this.openERPClient = openERPClient;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            OpenERPRequest openERPRequest = mapRequest(event);
            if (!openERPRequest.shouldERPConsumeEvent())
                return;

            openERPClient.execute(openERPRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        String encounterOrBedAssignmentEventContent = webClient.get(URI.create(urlPrefix + event.getContent()));

        List<WebClientResponseParser> encounterWebClientResponseParsers = new ArrayList<>();
        encounterWebClientResponseParsers.add(new OpenMRSEncounterParser(ObjectMapperRepository.objectMapper));
        encounterWebClientResponseParsers.add(new OpenMRSBedAssignmentParser(ObjectMapperRepository.objectMapper));

        OpenERPRequestParams openERPRequestParams = new OpenERPRequestParams(new ProductService(openERPClient), encounterWebClientResponseParsers);
        OpenERPRequest openERPRequest = openERPRequestParams.getRequest(encounterOrBedAssignmentEventContent,
                event.getFeedUri(), feedUrl, event.getId());

        if (event.getFeedUri() == null)
            openERPRequest.addParameter(createParameter("is_failed_event", "1", "boolean"));

        return openERPRequest;
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }
}
