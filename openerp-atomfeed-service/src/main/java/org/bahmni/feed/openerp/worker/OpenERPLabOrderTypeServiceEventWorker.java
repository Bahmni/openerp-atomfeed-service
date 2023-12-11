package org.bahmni.feed.openerp.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabOrderTypeEvent;
import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabPanelEvent;
import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSLabTestEvent;
import org.bahmni.feed.openerp.domain.labOrderType.OpenMRSRadiologyTestEvent;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OpenERPLabOrderTypeServiceEventWorker implements EventWorker {

    private static Logger logger = LoggerFactory.getLogger(OpenERPLabOrderTypeServiceEventWorker.class);
    private final OpenERPContext openERPContext;
    private final String feedUrl;
    private final String odooURL;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;
    private Map<String, OpenMRSLabOrderTypeEvent> labOrderTypeEventMap = new HashMap<>();


    public OpenERPLabOrderTypeServiceEventWorker(String feedUrl, String odooURL, OpenERPContext openERPContext, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPContext = openERPContext;
        this.feedUrl = feedUrl;
        this.odooURL = odooURL;
        this.webClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
        labOrderTypeEventMap.put(OpenMRSRadiologyTestEvent.RADIOLOGY_TEST_EVENT_NAME, new OpenMRSRadiologyTestEvent());
        labOrderTypeEventMap.put(OpenMRSLabTestEvent.LAB_TEST_EVENT_NAME, new OpenMRSLabTestEvent());
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
