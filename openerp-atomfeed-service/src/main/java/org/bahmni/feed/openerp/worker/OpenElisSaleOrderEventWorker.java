package org.bahmni.feed.openerp.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenElisWebClient;
import org.bahmni.feed.openerp.domain.openelis.OpenElisLabOrder;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;

public class OpenElisSaleOrderEventWorker implements EventWorker {
    private final String feedUrl;
    private final String odooURL;
    private final OpenERPContext openERPContext;
    private final OpenElisWebClient webClient;
    private final String urlPrefix;

    private static Logger logger = LoggerFactory.getLogger(OpenElisSaleOrderEventWorker.class);

    public OpenElisSaleOrderEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenElisWebClient webClient, String urlPrefix) {
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.openERPContext = openERPContext;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }


    @Override
    public void process(Event event) {
        try {
            OpenERPRequest openERPRequest = mapRequest(event);
            if (!openERPRequest.shouldERPConsumeEvent())
                return;
            openERPContext.execute(openERPRequest, odooURL);
        } catch (Exception e) {
            logger.error("Error processing openelis sale order event : {}", event.toString(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        String labOrders = webClient.get(URI.create(urlPrefix + event.getContent()));
        OpenElisLabOrder openElisLabOrder = ObjectMapperRepository.objectMapper.readValue(labOrders, OpenElisLabOrder.class);

        OpenERPRequest openERPRequest = new OpenERPRequest("atom.event.worker", "process_event", openElisLabOrder.getParameters(event.getId(), event.getFeedUri(), feedUrl));

        if (event.getFeedUri() == null)
            openERPRequest.addParameter(createParameter("is_failed_event", "1", "boolean"));

        return openERPRequest;
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }


}
