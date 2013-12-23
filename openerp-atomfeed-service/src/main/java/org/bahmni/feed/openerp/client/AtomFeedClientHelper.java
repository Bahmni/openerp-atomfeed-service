package org.bahmni.feed.openerp.client;

import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.webclients.ClientCookies;
import org.ict4h.atomfeed.client.factory.AtomFeedProperties;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;

public class AtomFeedClientHelper {
    private OpenMRSWebClient openMRSWebClient;
    private OpenERPAtomFeedProperties atomFeedProperties;
    private JdbcConnectionProvider jdbcConnectionProvider;
    private OpenERPClient openERPClient;
    private FeedClientFactory feedClientFactory;
    private AllMarkers allMarkers;
    private AllFeeds allFeeds;
    private AllFailedEvents allFailedEvents;

    public AtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, JdbcConnectionProvider jdbcConnectionProvider, OpenERPClient openERPClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        this.openERPClient = openERPClient;
    }

    AtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, JdbcConnectionProvider jdbcConnectionProvider, OpenERPClient openERPClient, FeedClientFactory feedClientFactory,OpenMRSWebClient openMRSWebClient, AllMarkers allMarkersJdbc, AllFeeds allFeeds, AllFailedEvents allFailedEvents) {
        this.atomFeedProperties = atomFeedProperties;
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        this.openERPClient = openERPClient;
        this.feedClientFactory = feedClientFactory;
        this.allMarkers = allMarkersJdbc;
        this.allFeeds = allFeeds;
        this.allFailedEvents = allFailedEvents;
        this.openMRSWebClient = openMRSWebClient;
    }

    public FeedClient getAtomFeedClient(String feedName, String jobName) throws FeedException {
        if(this.openMRSWebClient == null){
            this.openMRSWebClient = new OpenMRSWebClient(atomFeedProperties);
            feedClientFactory = new FeedClientFactory(openMRSWebClient);
        }
        return getAtomFeedClient(feedName, jobName,openMRSWebClient, feedClientFactory);
    }

    FeedClient getAtomFeedClient(String feedName, String jobName, OpenMRSWebClient openMRSWebClient, FeedClientFactory feedClientFactory) throws FeedException {

        ClientCookies cookies = openMRSWebClient.getCookies();
        if(cookies == null || cookies.size() == 0){
             return null;
        }
        allFeeds = getAllFeeds(atomFeedProperties, cookies);
        allMarkers = new AllMarkersJdbcImpl(jdbcConnectionProvider);
        allFailedEvents = new AllFailedEventsJdbcImpl(jdbcConnectionProvider);
        AtomFeedClient atomFeedClient = feedClientFactory.getFeedClient(atomFeedProperties, jdbcConnectionProvider, feedName, openERPClient, allFeeds, allMarkers, allFailedEvents, jobName);
        return atomFeedClient;
    }

    static AllFeeds getAllFeeds(OpenERPAtomFeedProperties atomFeedProperties, ClientCookies cookies) {
        AtomFeedProperties feedProperties = new AtomFeedProperties();
        feedProperties.setConnectTimeout(atomFeedProperties.getConnectionTimeoutInMilliseconds());
        feedProperties.setReadTimeout(atomFeedProperties.getReplyTimeoutInMilliseconds());
        feedProperties.setMaxFailedEvents(atomFeedProperties.getMaxFailedEvents());
        return new AllFeeds(feedProperties, cookies);
    }

   /* public void reInitializeAtomFeedClient(String feedName, String jobName) throws FeedException {
        initializeAtomFeedClientHelper(atomFeedProperties, jdbcConnectionProvider, openERPClient);
        return feedClientFactory.getFeedClient(atomFeedProperties, jdbcConnectionProvider, feedName, openERPClient, allFeeds, allMarkers, allFailedEvents, jobName);
    }*/
}
