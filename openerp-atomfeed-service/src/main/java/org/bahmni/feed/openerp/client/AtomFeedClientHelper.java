package org.bahmni.feed.openerp.client;

import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.worker.WorkerFactory;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.client.strategy.implementation.OpenERPXMLClient;
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
    private final OpenERPAtomFeedProperties atomFeedProperties;
    private final AtomFeedSpringTransactionSupport transactionManager;

    private final OpenERPXMLClient openERPXMLClient;

    private FeedClientFactory feedClientFactory;
    private final WebClientProvider webClientProvider;

    public AtomFeedClientHelper(OpenERPAtomFeedProperties atomFeedProperties, AtomFeedSpringTransactionSupport transactionManager, OpenERPXMLClient openERPXMLClient) {
        this.atomFeedProperties = atomFeedProperties;
        this.transactionManager = transactionManager;
        this.openERPXMLClient = openERPXMLClient;
        this.webClientProvider = new WebClientProvider(atomFeedProperties);
    }
    
    public FeedClient getAtomFeedClient(Jobs jobName) throws FeedException {
        if(this.feedClientFactory == null){
            WorkerFactory workerFactory = new WorkerFactory(webClientProvider);
            feedClientFactory = new FeedClientFactory(workerFactory);
        }
        return getAtomFeedClient(jobName, feedClientFactory);
    }

    FeedClient getAtomFeedClient(Jobs jobName, FeedClientFactory feedClientFactory) throws FeedException {
        ClientCookies cookies = webClientProvider.getWebClient(jobName).getCookies();
        AllFeeds allFeeds = getAllFeeds(atomFeedProperties, cookies);
        AllMarkers allMarkers = new AllMarkersJdbcImpl(transactionManager);
        AllFailedEvents allFailedEvents = new AllFailedEventsJdbcImpl(transactionManager);
        //TODO: Create a toggle between rest and xml client
        OpenERPContext openERPContext = new OpenERPContext(openERPXMLClient);
        return feedClientFactory.getFeedClient(atomFeedProperties, transactionManager, openERPContext, allFeeds, allMarkers, allFailedEvents, jobName);
    }

    static AllFeeds getAllFeeds(OpenERPAtomFeedProperties atomFeedProperties, ClientCookies cookies) {
        AtomFeedProperties feedProperties = new AtomFeedProperties();
        feedProperties.setConnectTimeout(atomFeedProperties.getConnectionTimeoutInMilliseconds());
        feedProperties.setReadTimeout(atomFeedProperties.getReplyTimeoutInMilliseconds());
        feedProperties.setMaxFailedEvents(atomFeedProperties.getMaxFailedEvents());
        return new AllFeeds(feedProperties, cookies);
    }

}
