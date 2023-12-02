package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

import static org.bahmni.feed.openerp.job.Jobs.SALEABLE_FEED;

public class OpenERPSaleableFeedJob {
    private SimpleFeedJob feedJob;

    public OpenERPSaleableFeedJob() {
    }

    public OpenERPSaleableFeedJob(SimpleFeedJob feedJob) throws FeedException {
        this.feedJob = feedJob;
    }

    public void processFeed()  {
        feedJob.processFeed(SALEABLE_FEED);
    }

    public void processFailedEvents()  {
        feedJob.processFailedEvents(SALEABLE_FEED);
    }

}
