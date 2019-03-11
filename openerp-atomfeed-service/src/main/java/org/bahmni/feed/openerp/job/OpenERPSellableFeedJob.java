package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPSellableFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    public OpenERPSellableFeedJob() {
    }

    public OpenERPSellableFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.SELLABLE_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.SELLABLE_FEED);
    }

}
