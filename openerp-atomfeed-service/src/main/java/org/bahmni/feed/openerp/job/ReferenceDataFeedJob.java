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

public class ReferenceDataFeedJob {
    private SimpleFeedJob simpleFeedJob;

    public ReferenceDataFeedJob() {
    }

    public ReferenceDataFeedJob(SimpleFeedJob simpleFeedJob) throws FeedException {
        this.simpleFeedJob = simpleFeedJob;
    }

    public void processFeed()  {
        simpleFeedJob.processFeed(Jobs.REFERENCE_DATA_FEED);
    }

    public void processFailedEvents()  {
        simpleFeedJob.processFailedEvents(Jobs.REFERENCE_DATA_FEED);
    }


}
