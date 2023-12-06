package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPLabTestFeedJob {

    private SimpleFeedJob openMRSFeedJob;

    public OpenERPLabTestFeedJob() {
    }

    public OpenERPLabTestFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.LAB_TEST_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.LAB_TEST_FEED);
    }

}
