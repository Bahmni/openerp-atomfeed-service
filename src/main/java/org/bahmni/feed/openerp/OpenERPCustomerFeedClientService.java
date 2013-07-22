package org.bahmni.feed.openerp;


import org.apache.log4j.Logger;
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
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequestMapping(value = "/openerp/**")
public class OpenERPCustomerFeedClientService {

    private AtomFeedClient atomFeedClient;
/*
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;
    private OpenERPClient openERPClient;
    private String feedName;
*/

    private static Logger logger = Logger.getLogger(OpenERPCustomerFeedClientService.class);

    public OpenERPCustomerFeedClientService (){
    }

    OpenERPCustomerFeedClientService(AtomFeedProperties atomFeedProperties, EventWorkerFactory workerFactory,
                                     OpenERPClient openERPClient, String feedName,
                                     AllFeeds allFeeds, AllMarkers allMarkers, AllFailedEvents allFailedEvents) {
        this.atomFeedClient = getFeedClient(atomFeedProperties, feedName, openERPClient, workerFactory, allFeeds, allMarkers, allFailedEvents);
    }

    @Autowired
    public OpenERPCustomerFeedClientService(AtomFeedProperties atomFeedProperties, OpenERPClient openERPClient, String feedName, JdbcConnectionProvider jdbcConnectionProvider) {
        this(atomFeedProperties, new EventWorkerFactory(), openERPClient, feedName,
                new AllFeeds(), new AllMarkersJdbcImpl(jdbcConnectionProvider), new AllFailedEventsJdbcImpl(jdbcConnectionProvider));
    }

    private static AtomFeedClient getFeedClient(AtomFeedProperties atomFeedProperties, String feedName,
                                                OpenERPClient openERPClient, EventWorkerFactory eventWorkerFactory,
                                                AllFeeds allFeeds, AllMarkers allMarkers, AllFailedEvents allFailedEvents) {
        String feedUri = atomFeedProperties.getFeedUri(feedName);
        try {
            EventWorker eventWorker = eventWorkerFactory.getWorker("openerp.customer.service", atomFeedProperties.getFeedUri(feedName),openERPClient);
            return new AtomFeedClient(allFeeds, allMarkers, allFailedEvents, new URI(feedUri), eventWorker) ;
        } catch (URISyntaxException e) {
            logger.error(e);
            throw new RuntimeException("error for uri:" + feedUri);
        }
    }

    public void processFeed()  {
        try {
            logger.info("Processing Customer Feed "+ DateTime.now());
            atomFeedClient.processEvents();
        } catch (Exception e) {
            logger.error("failed customer feed execution " + e);
        }
    }

}
