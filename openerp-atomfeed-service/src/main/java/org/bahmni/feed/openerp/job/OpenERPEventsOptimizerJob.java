package org.bahmni.feed.openerp.job;


import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.AllEventRecordsOffsetMarkers;
import org.ict4h.atomfeed.server.repository.ChunkingEntries;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.AllEventRecordsOffsetMarkersJdbcImpl;
import org.ict4h.atomfeed.server.repository.jdbc.ChunkingEntriesJdbcImpl;
import org.ict4h.atomfeed.server.service.NumberOffsetMarkerServiceImpl;
import org.ict4h.atomfeed.server.service.OffsetMarkerService;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;
import org.ict4h.atomfeed.transaction.AFTransactionWorkWithoutResult;

public class OpenERPEventsOptimizerJob {
    private static final int OFFSET_BY_NUMBER_OF_RECORDS_PER_CATEGORY = 1000;

    private AtomFeedSpringTransactionSupport transactionSupport;

    public OpenERPEventsOptimizerJob(AtomFeedSpringTransactionSupport transactionSupport) {
        this.transactionSupport = transactionSupport;
    }

    public void execute() {
        transactionSupport.executeWithTransaction(new AFTransactionWorkWithoutResult() {
            @Override
            protected void doInTransaction() {
                AllEventRecords allEventRecords = new AllEventRecordsJdbcImpl(transactionSupport);
                AllEventRecordsOffsetMarkers eventRecordsOffsetMarkers = new AllEventRecordsOffsetMarkersJdbcImpl(transactionSupport);
                ChunkingEntries chunkingEntries = new ChunkingEntriesJdbcImpl(transactionSupport);

                final OffsetMarkerService markerService = new NumberOffsetMarkerServiceImpl(allEventRecords, chunkingEntries, eventRecordsOffsetMarkers);
                markerService.markEvents(OFFSET_BY_NUMBER_OF_RECORDS_PER_CATEGORY);
            }
            @Override
            public PropagationDefinition getTxPropagationDefinition() {
                return PropagationDefinition.PROPAGATION_REQUIRED;
            }
        });
    }

}
