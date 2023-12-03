package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.client.WebClientProvider;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.ict4h.atomfeed.client.service.EventWorker;

public class WorkerFactory {

    private final WebClientProvider webClientProvider;

    public WorkerFactory(WebClientProvider webClientProvider) {
        this.webClientProvider = webClientProvider;
    }


    public EventWorker getWorker(Jobs jobName, String feedUrl, String odooURL, OpenERPContext openERPContext, String urlPrefix) {
        switch (jobName){
            case CUSTOMER_FEED: return new OpenERPCustomerServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEORDER_FEED: return new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case OPENELIS_SALEORDER_FEED: return  new OpenElisSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.openElisWebClient(), urlPrefix);
            case DRUG_FEED: return new OpenERPDrugServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case LAB_FEED: return new OpenERPLabOrderTypeServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEABLE_FEED: return new OpenERPSaleableResourceWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
        }
        throw new RuntimeException(String.format("No worker for %s", jobName));
    }
}
