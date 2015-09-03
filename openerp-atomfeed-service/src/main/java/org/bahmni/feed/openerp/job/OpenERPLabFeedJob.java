package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPLabFeedJob {

    private SimpleFeedJob openMRSFeedJob;

    private String feedName = "lab.feed.generator.uri";

    public OpenERPLabFeedJob() {
    }

    public OpenERPLabFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, Jobs.LAB_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, Jobs.LAB_FEED);
    }

}
