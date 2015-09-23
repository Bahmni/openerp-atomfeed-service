package org.bahmni.feed.openerp.job;

import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.AllEventRecordsOffsetMarkers;
import org.ict4h.atomfeed.server.repository.AllEventRecordsQueue;
import org.ict4h.atomfeed.server.repository.ChunkingEntries;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsOffsetMarkersJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsQueueJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.NumberOffsetMarkerServiceImpl;
import org.ict4h.atomfeed.server.service.OffsetMarkerService;
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
