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

public class OpenElisSaleOrderFeedJob {
    private SimpleFeedJob openElisFeedJob;

    public OpenElisSaleOrderFeedJob() {
    }

    public OpenElisSaleOrderFeedJob(SimpleFeedJob openElisFeedJob) throws FeedException {
        this.openElisFeedJob = openElisFeedJob;
    }

    public void processFeed()  {
        openElisFeedJob.processFeed(Jobs.OPENELIS_SALEORDER_FEED);
    }

    public void processFailedEvents()  {
        openElisFeedJob.processFailedEvents(Jobs.OPENELIS_SALEORDER_FEED);
    }

}
