package org.bahmni.feed.openerp.job;

import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.AllEventRecordsQueue;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsQueueJdbcImpl;
import org.ict4h.atomfeed.server.service.publisher.EventRecordsPublishingService;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;

public class OpenERPEventsPublisherJob {

    private AtomFeedSpringTransactionSupport atomFeedSpringTransactionSupport;

    public OpenERPEventsPublisherJob(AtomFeedSpringTransactionSupport atomFeedSpringTransactionSupport) {
        this.atomFeedSpringTransactionSupport = atomFeedSpringTransactionSupport;
    }

    public void execute() {
        AllEventRecords allEventRecords = new AllEventRecordsJdbcImpl(atomFeedSpringTransactionSupport);
        AllEventRecordsQueue allEventRecordsQueue = new AllEventRecordsQueueJdbcImpl(atomFeedSpringTransactionSupport);
        EventRecordsPublishingService.publish(allEventRecords, allEventRecordsQueue);
    }
}
