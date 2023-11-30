package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.job.Feed;
import org.bahmni.feed.openerp.client.WebClientProvider;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.ict4h.atomfeed.client.service.EventWorker;

public class WorkerFactory {

    private final WebClientProvider webClientProvider;

    public WorkerFactory(WebClientProvider webClientProvider) {
        this.webClientProvider = webClientProvider;
    }


    public EventWorker getWorker(Feed jobName, String feedUrl, String endpointURI, OpenERPContext openERPContext, String urlPrefix) {
        switch (jobName) {
            case CUSTOMER_FEED: return new OpenERPCustomerServiceEventWorker(feedUrl, endpointURI, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEORDER_FEED: return new OpenERPSaleOrderEventWorker(feedUrl, endpointURI, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case OPENELIS_SALEORDER_FEED: return new OpenElisSaleOrderEventWorker(feedUrl, endpointURI, openERPContext, webClientProvider.openElisWebClient(), urlPrefix);
            case DRUG_FEED: return new OpenERPDrugServiceEventWorker(feedUrl, endpointURI, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case LAB_FEED: return new OpenERPLabOrderTypeServiceEventWorker(feedUrl, endpointURI, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEABLE_FEED: return new OpenERPSaleableResourceWorker(feedUrl, endpointURI, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
        }
        throw new RuntimeException(String.format("No worker for %s", jobName));
    }
}
