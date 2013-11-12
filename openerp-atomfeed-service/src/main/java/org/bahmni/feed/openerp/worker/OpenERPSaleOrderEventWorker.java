package org.bahmni.feed.openerp.worker;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.OpenERPRequestParams;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
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
            openERPClient.execute(mapRequest(event));
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(Event event) {
    }

    public void processFailedEvents(Event event) {
        try {
            openERPClient.execute(mapFailedEventRequest(event));
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private OpenERPRequest mapFailedEventRequest(Event event) throws IOException {

        List<Parameter> parameterList = new OpenERPRequestParams(event, feedUrl).getParameters(getContent(event));
        parameterList.add(createParameter("is_failed_event", "True", "boolean"));
        return new OpenERPRequest("atom.event.worker", "process_event", parameterList);
    }

    private String getContent(Event event) {
        String content = event.getContent();
        return webClient.get(URI.create(urlPrefix + content), new HashMap<String, String>(0));
    }

    private OpenERPRequest mapRequest(Event event) throws IOException {
        return new OpenERPRequest("atom.event.worker", "process_event", new OpenERPRequestParams(event, feedUrl).getParameters(getContent(event)));
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }
}
