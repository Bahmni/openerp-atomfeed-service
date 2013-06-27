package org.bahmni.feed.openerp;


import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.factory.AtomClientFactory;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OpenERPCustomerFeedClientService {

    private AtomFeedClient atomFeedClient;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;
    private OpenERPClient openERPClient;
    Logger logger = Logger.getLogger(OpenERPCustomerFeedClientService.class);

    OpenERPCustomerFeedClientService(AtomFeedProperties atomFeedProperties, AtomFeedClient atomFeedClient, EventWorkerFactory workerFactory, OpenERPClient openERPClient) {
        this.workerFactory = workerFactory;
        this.atomFeedProperties = atomFeedProperties;
        this.atomFeedClient = atomFeedClient;
        this.workerFactory = new EventWorkerFactory();
        this.openERPClient = openERPClient;
    }

    @Autowired
    public OpenERPCustomerFeedClientService(AtomFeedProperties atomFeedProperties, OpenERPClient openERPClient) {
        this(atomFeedProperties, getFeedClient(), new EventWorkerFactory(), openERPClient);
    }

    private static AtomFeedClient getFeedClient() {
        JdbcConnectionProvider jdbcConnectionProvider = new PropertiesJdbcConnectionProvider();
        AllMarkersJdbcImpl allMarkersJdbc = new AllMarkersJdbcImpl(jdbcConnectionProvider);
        return new AtomClientFactory().create(allMarkersJdbc, new AllFailedEventsJdbcImpl(jdbcConnectionProvider));
    }

    @Scheduled(fixedDelay=3000)
    public void processFeed()  {
        EventWorker eventWorker = workerFactory.getWorker("openerp.customer.service", atomFeedProperties.getFeedUri(),openERPClient);
        try {
            logger.info("Processing Customer Feed "+ DateTime.now());
//            atomFeedClient.processEvents(new URI(atomFeedProperties.getFeedUri()), eventWorker);
        } catch (Exception e) {
            logger.error("failed customer feed execution " + e);
        // throw new RuntimeException(e);
        }
    }
}
