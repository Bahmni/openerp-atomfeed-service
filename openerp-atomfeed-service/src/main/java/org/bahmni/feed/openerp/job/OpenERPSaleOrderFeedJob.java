package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPSaleOrderFeedJob {
    private SimpleFeedJob openMRSFeedJob;
  //  public static final String JOB_NAME = "openerp.saleorder.service";
    private String feedName="saleorder.feed.generator.uri";

    public OpenERPSaleOrderFeedJob() {
    }

    public OpenERPSaleOrderFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, Jobs.SALEORDER_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, Jobs.SALEORDER_FEED);
    }
}
