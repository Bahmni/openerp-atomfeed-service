package org.bahmni.feed.openerp.event;

import org.bahmni.feed.openerp.AtomFeedProperties;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    private AtomFeedProperties atomFeedProperties;

    public EventWorker getWorker(String workerName, String feedUrl, OpenERPClient openERPClient) {
        if(workerName.equals("openerp.service"))
            return new OpenERPCustomerServiceEventWorker(feedUrl,openERPClient);
        return new EmptyWorker();
    }
}
