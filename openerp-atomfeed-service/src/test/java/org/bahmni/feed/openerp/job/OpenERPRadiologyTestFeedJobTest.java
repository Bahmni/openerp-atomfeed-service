/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.job;

import com.sun.syndication.io.FeedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.bahmni.feed.openerp.job.Jobs.RADIOLOGY_TEST_FEED;
import static org.mockito.Mockito.verify;

public class OpenERPRadiologyTestFeedJobTest {

    OpenERPRadiologyTestFeedJob radiologyTestFeedJob;

    @Mock
    SimpleFeedJob openMRSFeedJob;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessFeed() throws FeedException {
        radiologyTestFeedJob = new OpenERPRadiologyTestFeedJob(openMRSFeedJob);
        radiologyTestFeedJob.processFeed();
        verify(openMRSFeedJob).processFeed(RADIOLOGY_TEST_FEED);
    }

    @Test
    public void testProcessFailedEvents() throws FeedException {
        radiologyTestFeedJob = new OpenERPRadiologyTestFeedJob(openMRSFeedJob);
        radiologyTestFeedJob.processFailedEvents();
        verify(openMRSFeedJob).processFailedEvents(RADIOLOGY_TEST_FEED);
    }
}