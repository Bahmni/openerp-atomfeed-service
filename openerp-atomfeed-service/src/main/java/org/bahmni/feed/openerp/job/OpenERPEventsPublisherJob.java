/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.job;

import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.AllEventRecordsQueue;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsQueueJdbcImpl;
import org.ict4h.atomfeed.server.service.publisher.EventRecordsPublishingService;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;
import org.ict4h.atomfeed.transaction.AFTransactionWorkWithoutResult;

public class OpenERPEventsPublisherJob {

    private AtomFeedSpringTransactionSupport atomFeedSpringTransactionSupport;

    public OpenERPEventsPublisherJob(AtomFeedSpringTransactionSupport atomFeedSpringTransactionSupport) {
        this.atomFeedSpringTransactionSupport = atomFeedSpringTransactionSupport;
    }

    public void execute() {
        atomFeedSpringTransactionSupport.executeWithTransaction(new AFTransactionWorkWithoutResult() {
            @Override
            protected void doInTransaction() {
                AllEventRecords allEventRecords = new AllEventRecordsJdbcImpl(atomFeedSpringTransactionSupport);
                AllEventRecordsQueue allEventRecordsQueue = new AllEventRecordsQueueJdbcImpl(atomFeedSpringTransactionSupport);
                EventRecordsPublishingService.publish(allEventRecords, allEventRecordsQueue);
            }

            @Override
            public PropagationDefinition getTxPropagationDefinition() {
                return PropagationDefinition.PROPAGATION_REQUIRED;
            }
        });
    }
}
