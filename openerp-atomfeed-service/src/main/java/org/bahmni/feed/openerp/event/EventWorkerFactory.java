package org.bahmni.feed.openerp.event;

import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.webclients.WebClient;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    public EventWorker getWorker(String workerName, String feedUrl, OpenERPClient openERPClient,
                                 int connectionTimeoutInMilliseconds, int replyTimeoutInMilliseconds, String sessionIdKey, String sessionIdValue, String urlPrefix) {
        if (workerName.equals("openerp.customer.service"))
            return new OpenERPCustomerServiceEventWorker(feedUrl, openERPClient,
                    new WebClient(connectionTimeoutInMilliseconds, replyTimeoutInMilliseconds, sessionIdKey, sessionIdValue),
                    urlPrefix);
        throw new RuntimeException(String.format("No worker for %s", workerName));
    }
}
