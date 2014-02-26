package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenElisSaleOrderFeedJob {
    private SimpleFeedJob openElisFeedJob;
    private String feedName="openelis.saleorder.feed.generator.uri";

    public OpenElisSaleOrderFeedJob() {
    }

    public OpenElisSaleOrderFeedJob(SimpleFeedJob openElisFeedJob) throws FeedException {
        this.openElisFeedJob = openElisFeedJob;
    }

    public void processFeed()  {
        openElisFeedJob.processFeed(feedName, Jobs.OPENELIS_SALEORDER_FEED);
    }

    public void processFailedEvents()  {
        openElisFeedJob.processFailedEvents(feedName, Jobs.OPENELIS_SALEORDER_FEED);
    }

}
