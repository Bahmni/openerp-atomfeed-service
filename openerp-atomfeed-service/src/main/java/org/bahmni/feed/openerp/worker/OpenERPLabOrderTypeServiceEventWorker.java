package org.bahmni.feed.openerp.worker;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OpenERPLabOrderTypeServiceEventWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(OpenERPLabOrderTypeServiceEventWorker.class);

    private OpenERPClient openERPClient;
    private String feedUrl;
    private OpenMRSWebClient webClient;
    private String urlPrefix;
    private Map<String, OpenMRSLabOrderTypeEvent> labOrderTypeEventMap = new HashMap<>();


    public OpenERPLabOrderTypeServiceEventWorker(String feedUrl, OpenERPClient openERPClient, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPClient = openERPClient;
        this.feedUrl = feedUrl;
        this.webClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
        labOrderTypeEventMap.put(OpenMRSRadiologyTestEvent.RADIOLOGY_TEST_EVENT_NAME, new OpenMRSRadiologyTestEvent());
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing the event [" + event.getContent() + "]");
        try {
            OpenMRSLabOrderTypeEvent openMRSLabOrderTypeEvent = labOrderTypeEventMap.get(event.getTitle());
            if(openMRSLabOrderTypeEvent == null) return ;
            openERPClient.execute(mapRequest(event, openMRSLabOrderTypeEvent));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OpenERPRequest mapRequest(Event event, OpenMRSLabOrderTypeEvent openMRSLabOrderTypeEvent) throws IOException {
        String labOrderTypeJson = webClient.get(URI.create(urlPrefix + event.getContent()));
        return openMRSLabOrderTypeEvent.mapEventToOpenERPRequest(event, labOrderTypeJson);
    }
    @Override
    public void cleanUp(Event event) {

    }
}
