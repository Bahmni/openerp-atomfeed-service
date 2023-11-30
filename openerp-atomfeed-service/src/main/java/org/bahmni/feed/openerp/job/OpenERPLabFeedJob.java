package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPLabFeedJob {

    private SimpleFeedJob openMRSFeedJob;

    public OpenERPLabFeedJob() {
    }

    public OpenERPLabFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(FeedURI.LAB_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(FeedURI.LAB_FEED);
    }

}
