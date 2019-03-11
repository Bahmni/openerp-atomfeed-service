package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.Jobs.SELLABLE_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPSellableFeedJobTest {

    OpenERPSellableFeedJob sellableFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        sellableFeedJob = new OpenERPSellableFeedJob(openMRSFeedJob);
        sellableFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(SELLABLE_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        sellableFeedJob = new OpenERPSellableFeedJob(openMRSFeedJob);
        sellableFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(SELLABLE_FEED);
    }
}
