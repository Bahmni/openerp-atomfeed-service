package org.bahmni.feed.openerp.client;


import com.sun.syndication.io.FeedException;
import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.TaskMonitor;
import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class OpenERPCustomerFeedJob extends OpenMRSFeedJob {
    private static Logger logger = Logger.getLogger(OpenERPCustomerFeedJob.class);

    public OpenERPCustomerFeedJob() {}

    @Autowired
    public OpenERPCustomerFeedJob(OpenERPAtomFeedProperties atomFeedProperties, OpenERPClient openERPClient,
                                  String feedName, JdbcConnectionProvider jdbcConnectionProvider,
                                  org.bahmni.feed.openerp.TaskMonitor customerFeedClientMonitor) throws FeedException {
        this(atomFeedProperties,jdbcConnectionProvider, null, openERPClient, feedName,
                getAllFeeds(atomFeedProperties), new AllMarkersJdbcImpl(jdbcConnectionProvider),
                new AllFailedEventsJdbcImpl(jdbcConnectionProvider), customerFeedClientMonitor
                );
    }

    OpenERPCustomerFeedJob(OpenERPAtomFeedProperties atomFeedProperties, JdbcConnectionProvider jdbcConnectionProvider,
                           EventWorkerFactory workerFactory, OpenERPClient openERPClient, String feedName,
                           AllFeeds allFeeds, AllMarkers allMarkers, AllFailedEvents allFailedEvents,
                           TaskMonitor taskMonitor) throws FeedException {
        this.atomFeedProperties = atomFeedProperties;
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        this.workerFactory = workerFactory;
        this.openERPClient = openERPClient;
        this.feedName = feedName;
        this.allFeeds = allFeeds;
        this.allMarkers = allMarkers;
        this.allFailedEvents = allFailedEvents;
        this.taskMonitor = taskMonitor;
    }

    AtomFeedClient getFeedClient(OpenERPAtomFeedProperties atomFeedProperties,JdbcConnectionProvider jdbcConnectionProvider,
                                                String feedName, OpenERPClient openERPClient, EventWorkerFactory eventWorkerFactory,
                                                AllFeeds allFeeds, AllMarkers allMarkers, AllFailedEvents allFailedEvents) throws FeedException {
        String feedUri = atomFeedProperties.getFeedUri(feedName);
        try {
            String urlPrefix = getURLPrefix(atomFeedProperties);
            EventWorker eventWorker = eventWorkerFactory.getWorker("openerp.customer.service", atomFeedProperties.getFeedUri(feedName),openERPClient,
                     urlPrefix);
            return new AtomFeedClient(allFeeds, allMarkers, allFailedEvents, atomFeedProperties(), jdbcConnectionProvider, new URI(feedUri), eventWorker) ;
        } catch (URISyntaxException e) {
            logger.error(e);
            throw new RuntimeException("error for uri:" + feedUri);
        }
    }


}
