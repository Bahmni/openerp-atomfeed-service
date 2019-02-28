package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.bahmni.feed.openerp.Jobs;

public class SellableTypeFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    public SellableTypeFeedJob() {
    }

    public SellableTypeFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.SELLABLE_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.SELLABLE_FEED);
    }
}
