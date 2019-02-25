package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.bahmni.feed.openerp.Jobs;

public class OpenElisSaleOrderFeedJob {
    private SimpleFeedJob openElisFeedJob;

    public OpenElisSaleOrderFeedJob() {
    }

    public OpenElisSaleOrderFeedJob(SimpleFeedJob openElisFeedJob) throws FeedException {
        this.openElisFeedJob = openElisFeedJob;
    }

    public void processFeed()  {
        openElisFeedJob.processFeed(Jobs.OPENELIS_SALEORDER_FEED);
    }

    public void processFailedEvents()  {
        openElisFeedJob.processFailedEvents(Jobs.OPENELIS_SALEORDER_FEED);
    }

}
