package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.bahmni.feed.openerp.Jobs;

public class OpenERPSaleOrderFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    public OpenERPSaleOrderFeedJob() {
    }

    public OpenERPSaleOrderFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.SALEORDER_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.SALEORDER_FEED);
    }
}
