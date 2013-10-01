package org.bahmni.feed.openerp.event;

import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.webclients.WebClient;
import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {

    private WebClient webClient;

    public EventWorkerFactory(WebClient webClient){
        this.webClient = webClient;
    }

    public EventWorker getWorker(String workerName, String feedUrl, OpenERPClient openERPClient,
                                  String urlPrefix) {
        if (workerName.equals("openerp.customer.service"))
            return new OpenERPCustomerServiceEventWorker(feedUrl, openERPClient,
                    webClient,
                    urlPrefix);
        if (workerName.equals("openerp.saleorder.service"))
            return new OpenERPSaleOrderEventWorker(feedUrl, openERPClient,
                    webClient,
                    urlPrefix);
        throw new RuntimeException(String.format("No worker for %s", workerName));
    }
}
