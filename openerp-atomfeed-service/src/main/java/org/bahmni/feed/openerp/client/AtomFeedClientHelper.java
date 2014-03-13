package org.bahmni.feed.openerp.client;

import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.worker.WorkerFactory;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.webclients.ClientCookies;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;

public class AtomFeedClientHelper {
    private OpenERPAtomFeedProperties atomFeedProperties;
    private AtomFeedSpringTransactionSupport transactionManager;
    private OpenERPClient openERPClient;
    private FeedClientFactory feedClientFactory;
    private WebClientProvider webClientProvider;

    public AtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, AtomFeedSpringTransactionSupport transactionManager, OpenERPClient openERPClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.transactionManager = transactionManager;
        this.openERPClient = openERPClient;
        this.webClientProvider = new WebClientProvider(atomFeedProperties);
    }
    
    public FeedClient getAtomFeedClient(String feedName, Jobs jobName) throws FeedException {
        if(this.feedClientFactory == null){
            WorkerFactory workerFactory = new WorkerFactory(webClientProvider);
            feedClientFactory = new FeedClientFactory(workerFactory);
        }
        return getAtomFeedClient(feedName, jobName, feedClientFactory);
    }

    FeedClient getAtomFeedClient(String feedName, Jobs jobName, FeedClientFactory feedClientFactory) throws FeedException {
        ClientCookies cookies = webClientProvider.getWebClient(jobName).getCookies();
        AllFeeds allFeeds = getAllFeeds(atomFeedProperties, cookies);
        AllMarkers allMarkers = new AllMarkersJdbcImpl(transactionManager);
        AllFailedEvents allFailedEvents = new AllFailedEventsJdbcImpl(transactionManager);
        return feedClientFactory.getFeedClient(atomFeedProperties, transactionManager, feedName, openERPClient, allFeeds, allMarkers, allFailedEvents, jobName);
    }

    static AllFeeds getAllFeeds(OpenERPAtomFeedProperties atomFeedProperties, ClientCookies cookies) {
        AtomFeedProperties feedProperties = new AtomFeedProperties();
        feedProperties.setConnectTimeout(atomFeedProperties.getConnectionTimeoutInMilliseconds());
        feedProperties.setReadTimeout(atomFeedProperties.getReplyTimeoutInMilliseconds());
        feedProperties.setMaxFailedEvents(atomFeedProperties.getMaxFailedEvents());
        return new AllFeeds(feedProperties, cookies);
    }

}
