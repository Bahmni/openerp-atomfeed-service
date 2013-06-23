package org.bahmni.feed.openerp;


import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;

public class OpenERPAtomFeedClientService {

    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;

    @Autowired
    public OpenERPAtomFeedClientService(AtomFeedProperties atomFeedProperties,AtomFeedClient atomFeedClient,EventWorkerFactory workerFactory) {
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
        this.workerFactory = workerFactory;
    }


    public void processFeed() throws URISyntaxException {
        EventWorker eventWorker = workerFactory.getWorker("openerp.service");
        atomFeedClient.processEvents(new URI(atomFeedProperties.getFeedUri()), eventWorker);
    }
}
