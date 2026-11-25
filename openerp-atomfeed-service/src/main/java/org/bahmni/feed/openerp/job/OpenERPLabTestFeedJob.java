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

public class OpenERPLabTestFeedJob {

    private SimpleFeedJob openMRSFeedJob;

    public OpenERPLabTestFeedJob() {
    }

    public OpenERPLabTestFeedJob(SimpleFeedJob openMRSFeedJob) throws FeedException {
        this.openMRSFeedJob = openMRSFeedJob;
    }

    public void processFeed()  {
        openMRSFeedJob.processFeed(Jobs.LAB_TEST_FEED);
    }

    public void processFailedEvents()  {
        openMRSFeedJob.processFailedEvents(Jobs.LAB_TEST_FEED);
    }

}
