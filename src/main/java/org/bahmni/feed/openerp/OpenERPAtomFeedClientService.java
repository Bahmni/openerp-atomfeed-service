package org.bahmni.feed.openerp;


import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

public class OpenERPAtomFeedClientService {

    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;
    private OpenERPClient openERPClient;

    OpenERPAtomFeedClientService(AtomFeedProperties atomFeedProperties,AtomFeedClient atomFeedClient,EventWorkerFactory workerFactory,OpenERPClient openERPClient) {
        this(atomFeedProperties, atomFeedClient, openERPClient);
        this.workerFactory = workerFactory;
    }

    @Autowired
    public OpenERPAtomFeedClientService(AtomFeedProperties atomFeedProperties,AtomFeedClient atomFeedClient,OpenERPClient openERPClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
        this.workerFactory = new EventWorkerFactory();
        this.openERPClient = openERPClient;
    }


    @Scheduled(fixedDelay=180000)
    public void processFeed() throws URISyntaxException {
        EventWorker eventWorker = workerFactory.getWorker("openerp.customer.service", atomFeedProperties.getFeedUri(),openERPClient);
        atomFeedClient.processEvents(new URI(atomFeedProperties.getFeedUri()), eventWorker);
    }
}
