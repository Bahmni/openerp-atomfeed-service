package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPCustomerFeedJob {
    private SimpleFeedJob openMRSFeedJob;
    //public static final String JOB_NAME = "openerp.customer.service";

    private String feedName = "customer.feed.generator.uri";

    public OpenERPCustomerFeedJob() {
    }

    public OpenERPCustomerFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, Jobs.CUSTOMER_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, Jobs.CUSTOMER_FEED);
    }

}
