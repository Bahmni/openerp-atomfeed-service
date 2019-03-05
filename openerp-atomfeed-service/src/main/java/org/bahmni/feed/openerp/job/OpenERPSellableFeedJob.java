package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPSellableFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    private String feedName = "sellable.feed.generator.uri";

    public OpenERPSellableFeedJob() {
    }

    public OpenERPSellableFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, Jobs.SELLABLE_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, Jobs.SELLABLE_FEED);
    }

}
