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
    private AtomFeedClient atomFeedClient;
    private OpenMRSWebClient openMRSWebClient;
    private AllFeeds allFeeds;
    private AllFailedEvents allFailedEvents;
    private AllMarkers allMarkers;
    private OpenERPAtomFeedProperties atomFeedProperties;
    private JdbcConnectionProvider jdbcConnectionProvider;
    private FeedClientFactory feedClientFactory;
    private OpenERPClient openERPClient;

    public AtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, JdbcConnectionProvider jdbcConnectionProvider,OpenERPClient openERPClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        this.openERPClient = openERPClient;
    }

    AtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, JdbcConnectionProvider jdbcConnectionProvider,OpenERPClient openERPClient,FeedClientFactory feedClientFactory,AllMarkers allMarkers,AllFeeds allFeeds,AllFailedEvents allFailedEvents){
        this.atomFeedProperties = atomFeedProperties;
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        this.openERPClient = openERPClient;
        this.feedClientFactory = feedClientFactory;
        this.allMarkers = allMarkers;
        this.allFeeds = allFeeds;
        this.allFailedEvents = allFailedEvents;
    }

    private void initializeAtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, JdbcConnectionProvider jdbcConnectionProvider, OpenERPClient openERPClient) {
        this.openMRSWebClient = new OpenMRSWebClient(atomFeedProperties);
        this.atomFeedProperties = atomFeedProperties;
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        this.openERPClient = openERPClient;
        this.feedClientFactory =  new FeedClientFactory(openMRSWebClient);
        this.allFeeds = getAllFeeds(atomFeedProperties, openMRSWebClient.getCookies());
        if(this.allMarkers == null) {
            this.allMarkers = new AllMarkersJdbcImpl(jdbcConnectionProvider);
        }
        if(this.allFailedEvents == null) {
            this.allFailedEvents = new AllFailedEventsJdbcImpl(jdbcConnectionProvider);
        }
    }

    public FeedClient getAtomFeedClient(String feedName,String jobName) throws FeedException {
        if (atomFeedClient == null) {
            if(feedClientFactory == null) {
                initializeAtomFeedClientHelper(atomFeedProperties, jdbcConnectionProvider, openERPClient);
            }
            atomFeedClient = feedClientFactory.getFeedClient(atomFeedProperties, jdbcConnectionProvider, feedName, openERPClient, allFeeds, allMarkers, allFailedEvents, jobName);
        }
        return atomFeedClient;
    }

    static AllFeeds getAllFeeds(OpenERPAtomFeedProperties atomFeedProperties, ClientCookies cookies) {
        AtomFeedProperties feedProperties = new AtomFeedProperties();
        feedProperties.setConnectTimeout(atomFeedProperties.getConnectionTimeoutInMilliseconds());
        feedProperties.setReadTimeout(atomFeedProperties.getReplyTimeoutInMilliseconds());
        feedProperties.setMaxFailedEvents(atomFeedProperties.getMaxFailedEvents());
        return new AllFeeds(feedProperties, cookies);
    }

    public void reInitializeAtomFeedClient(String feedName,String jobName) throws FeedException {
        initializeAtomFeedClientHelper(atomFeedProperties, jdbcConnectionProvider, openERPClient);
        atomFeedClient = feedClientFactory.getFeedClient(atomFeedProperties, jdbcConnectionProvider, feedName, openERPClient, allFeeds, allMarkers, allFailedEvents, jobName);
    }
}
