package org.bahmni.feed.openerp.event;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    private OpenERPAtomFeedProperties atomFeedProperties;

    public EventWorker getWorker(String workerName, String feedUrl, OpenERPClient openERPClient) {
        if(workerName.equals("openerp.customer.service"))
            return new OpenERPCustomerServiceEventWorker(feedUrl,openERPClient);
        return new EmptyWorker();
    }
}
