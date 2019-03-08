package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPCustomerFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    public OpenERPCustomerFeedJob() {
    }

    public OpenERPCustomerFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.CUSTOMER_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.CUSTOMER_FEED);
    }

}
