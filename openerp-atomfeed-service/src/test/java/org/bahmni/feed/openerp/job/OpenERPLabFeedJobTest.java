package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.Feed.LAB_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPLabFeedJobTest {

    OpenERPLabFeedJob labFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        labFeedJob = new OpenERPLabFeedJob(openMRSFeedJob);
        labFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(LAB_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        labFeedJob = new OpenERPLabFeedJob(openMRSFeedJob);
        labFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(LAB_FEED);
    }
}
