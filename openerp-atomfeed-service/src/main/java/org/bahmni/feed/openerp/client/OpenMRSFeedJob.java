package org.bahmni.feed.openerp.client;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.TaskMonitor;
import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.webclients.WebClient;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;
import org.ict4h.atomfeed.client.factory.AtomFeedProperties;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.joda.time.DateTime;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public abstract class OpenMRSFeedJob {
    private static Logger logger = Logger.getLogger(OpenMRSFeedJob.class);
    public static final String JSESSION_ID_KEY = "JSESSIONID";

    AtomFeedClient atomFeedClient;
    TaskMonitor taskMonitor;
    OpenERPAtomFeedProperties atomFeedProperties;
    JdbcConnectionProvider jdbcConnectionProvider;
    EventWorkerFactory workerFactory;
    OpenERPClient openERPClient;
    String feedName;
    AllFeeds allFeeds;
    AllMarkers allMarkers;
    AllFailedEvents allFailedEvents;
    OpenMRSAuthenticator openMRSAuthenticator;

    public OpenMRSFeedJob() {}


    public void processFeed()  {
        try {
            taskMonitor.startTask();
            logger.info("Processing Customer Feed "+ DateTime.now());

            getAtomFeedClient().processEvents();
        } catch (Exception e) {
            logger.error("failed customer feed execution " + e);
            handleAuthorizationException(e);
        } finally {
            taskMonitor.endTask();
        }
    }

    public void processFailedEvents()  {
        try {
            taskMonitor.startTask();
            logger.info("Processing failed events for Customer Feed "+ DateTime.now());

            getAtomFeedClient().processFailedEvents();
        } catch (Exception e) {
            logger.error("failed customer feed execution " + e);
            handleAuthorizationException(e);
        } finally {
            taskMonitor.endTask();
        }
    }

    protected void handleAuthorizationException(Throwable e) throws FeedException {
        if (e != null && ExceptionUtils.getStackTrace(e).contains("HTTP response code: 401")) {
            reInitializeAtomFeedClient();
        }
    }

    private void reInitializeAtomFeedClient() throws FeedException {
        workerFactory = new EventWorkerFactory(getWebClient(atomFeedProperties,new OpenMRSAuthenticator(atomFeedProperties.getAuthenticationURI(), atomFeedProperties.getConnectionTimeoutInMilliseconds(), atomFeedProperties.getReplyTimeoutInMilliseconds())));
        atomFeedClient = getFeedClient(atomFeedProperties,jdbcConnectionProvider, feedName, openERPClient, workerFactory, allFeeds, allMarkers, allFailedEvents);
    }

    private FeedClient getAtomFeedClient() throws FeedException {
        if (atomFeedClient == null) {
            if(workerFactory == null)
                workerFactory = new EventWorkerFactory(getWebClient(atomFeedProperties,new OpenMRSAuthenticator(atomFeedProperties.getAuthenticationURI(), atomFeedProperties.getConnectionTimeoutInMilliseconds(), atomFeedProperties.getReplyTimeoutInMilliseconds())));
            atomFeedClient = getFeedClient(atomFeedProperties,jdbcConnectionProvider, feedName, openERPClient, workerFactory, allFeeds, allMarkers, allFailedEvents);
        }
        return atomFeedClient;
    }

    static AllFeeds getAllFeeds(OpenERPAtomFeedProperties atomFeedProperties) {
        AtomFeedProperties feedProperties = new AtomFeedProperties();
        feedProperties.setConnectTimeout(atomFeedProperties.getConnectionTimeoutInMilliseconds());
        feedProperties.setReadTimeout(atomFeedProperties.getReplyTimeoutInMilliseconds());
        feedProperties.setMaxFailedEvents(atomFeedProperties.getMaxFailedEvents());
        return new AllFeeds(feedProperties, new HashMap<String, String>());
    }

    abstract AtomFeedClient getFeedClient(OpenERPAtomFeedProperties atomFeedProperties,JdbcConnectionProvider jdbcConnectionProvider,
                                                String feedName, OpenERPClient openERPClient, EventWorkerFactory eventWorkerFactory,
                                                AllFeeds allFeeds, AllMarkers allMarkers, AllFailedEvents allFailedEvents) throws FeedException ;

    static WebClient getWebClient(OpenERPAtomFeedProperties atomFeedProperties,OpenMRSAuthenticator openMRSAuthenticator) {
        OpenMRSAuthenticationResponse authenticationResponse = openMRSAuthenticator.authenticate(atomFeedProperties.getOpenMRSUser(),
                atomFeedProperties.getOpenMRSPassword(), ObjectMapperRepository.objectMapper);
        if (!authenticationResponse.isAuthenticated()) throw new FeedException("Failed to authenticate with OpenMRS");
        String sessionIdValue = authenticationResponse.getSessionId();

        return new WebClient(atomFeedProperties.getConnectionTimeoutInMilliseconds(), atomFeedProperties.getReplyTimeoutInMilliseconds(), JSESSION_ID_KEY, sessionIdValue);
    }

    static AtomFeedProperties atomFeedProperties() {
        AtomFeedProperties atomFeedProperties = new AtomFeedProperties();
        atomFeedProperties.setControlsEventProcessing(true);
        return atomFeedProperties;
    }

    static String getURLPrefix(OpenERPAtomFeedProperties atomFeedProperties) {
        String authenticationURI = atomFeedProperties.getAuthenticationURI();
        try {
            URL openMRSAuthURL = new URL(authenticationURI);
            return String.format("%s://%s", openMRSAuthURL.getProtocol(), openMRSAuthURL.getAuthority());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Is not a valid URI - " + authenticationURI);
        }
    }

    void setAtomFeedClient(AtomFeedClient atomFeedClient) {
        this.atomFeedClient = atomFeedClient;
    }


}
