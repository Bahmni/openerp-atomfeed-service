package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;

public class OpenERPLabPanelFeedJob {
    private SimpleFeedJob openMRSFeedJob;

    public OpenERPLabPanelFeedJob() {
    }

    public OpenERPLabPanelFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.LAB_PANEL_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.LAB_PANEL_FEED);
    }
}
