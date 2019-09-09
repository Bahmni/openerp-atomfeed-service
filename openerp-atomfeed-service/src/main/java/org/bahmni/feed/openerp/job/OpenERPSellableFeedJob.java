package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

import static org.bahmni.feed.openerp.job.Jobs.SELLABLE_FEED;

public class OpenERPSellableFeedJob {
    private SimpleFeedJob feedJob;

    public OpenERPSellableFeedJob() {
    }

    public OpenERPSellableFeedJob(SimpleFeedJob feedJob) throws FeedException {
        this.feedJob = feedJob;
    }

    public void processFeed()  {
        feedJob.processFeed(SELLABLE_FEED);
    }

    public void processFailedEvents()  {
        feedJob.processFailedEvents(SELLABLE_FEED);
    }

}
