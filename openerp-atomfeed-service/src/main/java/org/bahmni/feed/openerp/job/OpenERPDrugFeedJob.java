package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPDrugFeedJob {

    private SimpleFeedJob openMRSFeedJob;

    public OpenERPDrugFeedJob() {
    }

    public OpenERPDrugFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Feed.DRUG_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Feed.DRUG_FEED);
    }

}
