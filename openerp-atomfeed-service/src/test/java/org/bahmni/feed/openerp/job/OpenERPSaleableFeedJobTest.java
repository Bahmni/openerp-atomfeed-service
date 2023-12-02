package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.Jobs.SALEABLE_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPSaleableFeedJobTest {

    OpenERPSaleableFeedJob saleableFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        saleableFeedJob = new OpenERPSaleableFeedJob(openMRSFeedJob);
        saleableFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(SALEABLE_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        saleableFeedJob = new OpenERPSaleableFeedJob(openMRSFeedJob);
        saleableFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(SALEABLE_FEED);
    }
}
