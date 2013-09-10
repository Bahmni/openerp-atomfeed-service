package org.bahmni.feed.openerp.event;

import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    public EventWorker getWorker(String workerName, String feedUrl, OpenERPClient openERPClient) {
        if (workerName.equals("openerp.customer.service"))
            return new OpenERPCustomerServiceEventWorker(feedUrl, openERPClient);
        throw new RuntimeException(String.format("No worker for %s", workerName));
    }
}
