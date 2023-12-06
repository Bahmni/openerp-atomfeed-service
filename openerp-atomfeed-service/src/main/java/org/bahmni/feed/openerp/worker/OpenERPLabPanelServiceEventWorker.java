package org.bahmni.feed.openerp.worker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabOrderTypeEvent;
import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabPanelEvent;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OpenERPLabPanelServiceEventWorker implements EventWorker {

    private static final Logger logger = LogManager.getLogger(OpenERPLabPanelServiceEventWorker.class);
    private final OpenERPContext openERPContext;
    private final String feedUrl;
    private final String odooURL;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;
    private Map<String, OpenMRSLabOrderTypeEvent> labOrderTypeEventMap = new HashMap<>();


    public OpenERPLabPanelServiceEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPContext = openERPContext;
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.webClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
        labOrderTypeEventMap.put(OpenMRSLabPanelEvent.LAB_PANEL_EVENT_NAME, new OpenMRSLabPanelEvent());
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing the event [{}]", event.getContent());
        try {
            OpenMRSLabOrderTypeEvent openMRSLabOrderTypeEvent = labOrderTypeEventMap.get(event.getTitle());
            if(openMRSLabOrderTypeEvent == null) return ;
            openERPContext.execute(mapRequest(event, openMRSLabOrderTypeEvent), odooURL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private OpenERPRequest mapRequest(Event event, OpenMRSLabOrderTypeEvent openMRSLabOrderTypeEvent) throws IOException {
        String labOrderTypeJson = webClient.get(URI.create(urlPrefix + event.getContent()));
        return openMRSLabOrderTypeEvent.mapEventToOpenERPRequest(event, labOrderTypeJson, feedUrl);
    }
    @Override
    public void cleanUp(Event event) {

    }
}
