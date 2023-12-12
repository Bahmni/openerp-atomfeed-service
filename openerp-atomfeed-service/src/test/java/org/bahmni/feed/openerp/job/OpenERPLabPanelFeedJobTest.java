package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.Jobs.LAB_PANEL_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPLabPanelFeedJobTest {

    OpenERPLabPanelFeedJob labPanelFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        labPanelFeedJob = new OpenERPLabPanelFeedJob(openMRSFeedJob);
        labPanelFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(LAB_PANEL_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        labPanelFeedJob = new OpenERPLabPanelFeedJob(openMRSFeedJob);
        labPanelFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(LAB_PANEL_FEED);
    }
}