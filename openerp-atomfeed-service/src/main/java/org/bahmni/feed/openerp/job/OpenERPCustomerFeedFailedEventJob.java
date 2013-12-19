package org.bahmni.feed.openerp.job;


import com.sun.syndication.io.FeedException;

public class OpenERPCustomerFeedFailedEventJob {
    private OpenMRSFeedJob openMRSFeedJob;
    private static final String jobName = "openerp.customer.service";
    private static final String feedName = "customer.feed.generator.uri";

    public OpenERPCustomerFeedFailedEventJob() {
    }

    public OpenERPCustomerFeedFailedEventJob(OpenMRSFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, jobName);
    }

}
