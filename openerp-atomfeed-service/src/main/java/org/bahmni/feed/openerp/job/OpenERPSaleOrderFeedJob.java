package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPSaleOrderFeedJob {
    private OpenMRSFeedJob openMRSFeedJob;
    private static final String jobName = "openerp.saleorder.service";
    private String feedName="saleorder.feed.generator.uri";

    public OpenERPSaleOrderFeedJob() {
    }

    public OpenERPSaleOrderFeedJob(OpenMRSFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, jobName);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, jobName);
    }
}
