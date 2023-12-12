package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.Jobs.LAB_TEST_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPLabTestFeedJobTest {

    OpenERPLabTestFeedJob labTestFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        labTestFeedJob = new OpenERPLabTestFeedJob(openMRSFeedJob);
        labTestFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(LAB_TEST_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        labTestFeedJob = new OpenERPLabTestFeedJob(openMRSFeedJob);
        labTestFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(LAB_TEST_FEED);
    }
}