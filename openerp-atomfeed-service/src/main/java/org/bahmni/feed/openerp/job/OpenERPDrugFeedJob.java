package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPDrugFeedJob {

    private SimpleFeedJob openMRSFeedJob;

    private String feedName = "drug.feed.generator.uri";

    public OpenERPDrugFeedJob() {
    }

    public OpenERPDrugFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(feedName, Jobs.DRUG_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(feedName, Jobs.DRUG_FEED);
    }

}
