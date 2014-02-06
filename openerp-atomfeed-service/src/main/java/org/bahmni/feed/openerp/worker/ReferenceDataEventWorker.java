package org.bahmni.feed.openerp.worker;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.ReferenceDataWebClient;
import org.bahmni.feed.openerp.domain.referencedata.parser.ReferenceDataResponseParser;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;

public class ReferenceDataEventWorker implements EventWorker {
    private final String feedUrl;
    private final OpenERPClient openERPClient;
    private final String urlPrefix;
    private ReferenceDataWebClient webClient;

    private static Logger logger = Logger.getLogger(ReferenceDataEventWorker.class);


    public ReferenceDataEventWorker(String feedUrl, OpenERPClient openERPClient, ReferenceDataWebClient webClient, String urlPrefix) {
        this.feedUrl = feedUrl;
        this.openERPClient = openERPClient;
        this.webClient = webClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        try {
            OpenERPRequest openERPRequest = mapToERPRequest(event);
            openERPClient.execute(openERPRequest);
        } catch (IOException e) {
            logger.error("Error processing reference data event : "+event.toString(),e);
        }
    }

    private OpenERPRequest mapToERPRequest(Event event) throws IOException {
        String referenceDataAsString  = webClient.get(URI.create(urlPrefix + event.getContent()+".json"));
        ObjectMapper objectMapper = ObjectMapperRepository.objectMapper;
        return new ReferenceDataResponseParser(objectMapper).parse(referenceDataAsString,event.getTitle(),null,event.getId(),event.getFeedUri(),feedUrl);
    }

    @Override
    public void cleanUp(Event event) {

    }
}
