package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.client.WebClientProvider;
import org.bahmni.feed.openerp.job.OpenElisSaleOrderFeedJob;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.service.EventWorker;

public class WorkerFactory {

    private WebClientProvider webClientProvider;

    public WorkerFactory(WebClientProvider webClientProvider) {
        this.webClientProvider = webClientProvider;
    }


    public EventWorker getWorker(Jobs jobName, String feedUrl, OpenERPClient openERPClient,
                                 String urlPrefix) {

        switch (jobName){
            case CUSTOMER_FEED: return new OpenERPCustomerServiceEventWorker(feedUrl, openERPClient, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEORDER_FEED: return new OpenERPSaleOrderEventWorker(feedUrl, openERPClient, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case OPENELIS_SALEORDER_FEED: return  new OpenElisSaleOrderEventWorker(feedUrl, openERPClient, webClientProvider.openElisWebClient(), urlPrefix);
            case DRUG_FEED: return new OpenERPDrugServiceEventWorker(feedUrl, openERPClient, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case LAB_FEED: return new OpenERPLabOrderTypeServiceEventWorker(feedUrl, openERPClient, webClientProvider.getOpenMRSWebClient(), urlPrefix);
        }

        throw new RuntimeException(String.format("No worker for %s", jobName));
    }



}
