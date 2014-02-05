package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPCustomerFeedJob {
    private OpenMRSFeedJob openMRSFeedJob;
    private static final String jobName = "openerp.customer.service";
    private String feedName = "customer.feed.generator.uri";

    public OpenERPCustomerFeedJob() {
    }

    public OpenERPCustomerFeedJob(OpenMRSFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, jobName);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, jobName);
    }

}
