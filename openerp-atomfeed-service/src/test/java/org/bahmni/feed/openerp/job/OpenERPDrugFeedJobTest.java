package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.FeedURI.DRUG_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPDrugFeedJobTest {

    OpenERPDrugFeedJob drugFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        drugFeedJob = new OpenERPDrugFeedJob(openMRSFeedJob);
        drugFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(DRUG_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        drugFeedJob = new OpenERPDrugFeedJob(openMRSFeedJob);
        drugFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(DRUG_FEED);
    }
}
