package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPSaleOrderFeedFailedEventJob {
    private OpenMRSFeedJob openMRSFeedJob;
    private static final String jobName = "openerp.saleorder.service";
    private static final String feedName = "saleorder.feed.generator.uri";

    public OpenERPSaleOrderFeedFailedEventJob() {
    }

    public OpenERPSaleOrderFeedFailedEventJob(OpenMRSFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, jobName);
    }
}
