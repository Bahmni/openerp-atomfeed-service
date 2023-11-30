package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenElisSaleOrderFeedJob {
    private SimpleFeedJob openElisFeedJob;

    public OpenElisSaleOrderFeedJob() {
    }

    public OpenElisSaleOrderFeedJob(SimpleFeedJob openElisFeedJob) throws FeedException {
        this.openElisFeedJob = openElisFeedJob;
    }

    public void processFeed()  {
        openElisFeedJob.processFeed(FeedURI.OPENELIS_SALEORDER_FEED);
    }

    public void processFailedEvents()  {
        openElisFeedJob.processFailedEvents(FeedURI.OPENELIS_SALEORDER_FEED);
    }

}
