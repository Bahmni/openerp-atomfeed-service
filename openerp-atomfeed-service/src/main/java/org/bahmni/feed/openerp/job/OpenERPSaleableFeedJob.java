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

import static org.bahmni.feed.openerp.job.Jobs.SALEABLE_FEED;

public class OpenERPSaleableFeedJob {
    private SimpleFeedJob feedJob;

    public OpenERPSaleableFeedJob() {
    }

    public OpenERPSaleableFeedJob(SimpleFeedJob feedJob) throws FeedException {
        this.feedJob = feedJob;
    }

    public void processFeed()  {
        feedJob.processFeed(SALEABLE_FEED);
    }

    public void processFailedEvents()  {
        feedJob.processFailedEvents(SALEABLE_FEED);
    }

}
