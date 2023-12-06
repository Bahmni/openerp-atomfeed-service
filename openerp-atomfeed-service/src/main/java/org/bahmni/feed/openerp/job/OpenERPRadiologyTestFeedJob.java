package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPRadiologyTestFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    public OpenERPRadiologyTestFeedJob() {
    }

    public OpenERPRadiologyTestFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.RADIOLOGY_TEST_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.RADIOLOGY_TEST_FEED);
    }

}
