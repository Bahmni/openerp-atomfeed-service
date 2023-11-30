package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class ReferenceDataFeedJob {
    private SimpleFeedJob simpleFeedJob;

    public ReferenceDataFeedJob() {
    }

    public ReferenceDataFeedJob(SimpleFeedJob simpleFeedJob) throws FeedException {
        this.simpleFeedJob = simpleFeedJob;
    }

    public void processFeed()  {
        simpleFeedJob.processFeed(Feed.REFERENCE_DATA_FEED);
    }

    public void processFailedEvents()  {
        simpleFeedJob.processFailedEvents(Feed.REFERENCE_DATA_FEED);
    }


}
