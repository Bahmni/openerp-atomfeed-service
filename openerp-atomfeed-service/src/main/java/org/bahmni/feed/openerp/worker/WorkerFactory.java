package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.client.WebClientProvider;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.springframework.context.ApplicationContext;

public class WorkerFactory {

    private final WebClientProvider webClientProvider;
    private final ApplicationContext applicationContext;

    public WorkerFactory(WebClientProvider webClientProvider, ApplicationContext applicationContext) {
        this.webClientProvider = webClientProvider;
        this.applicationContext = applicationContext;
    }


    public EventWorker getWorker(Jobs jobName, String feedUrl, String odooURL, OpenERPContext openERPContext, String urlPrefix, OpenERPAtomFeedProperties openERPAtomFeedProperties, Boolean isOdoo16) {
        switch (jobName){
            case CUSTOMER_FEED: return new OpenERPCustomerServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEORDER_FEED: return new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix, openERPAtomFeedProperties, isOdoo16, applicationContext);
            case OPENELIS_SALEORDER_FEED: return  new OpenElisSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.openElisWebClient(), urlPrefix);
            case DRUG_FEED: return new OpenERPDrugServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case LAB_TEST_FEED: return new OpenERPLabTestServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case LAB_PANEL_FEED: return new OpenERPLabPanelServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case RADIOLOGY_TEST_FEED: return new OpenERPRadiologyTestServiceEventWorker(feedUrl, odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
            case SALEABLE_FEED: return new OpenERPSaleableResourceWorker(odooURL, openERPContext, webClientProvider.getOpenMRSWebClient(), urlPrefix);
        }
        throw new RuntimeException(String.format("No worker for %s", jobName));
    }
}
