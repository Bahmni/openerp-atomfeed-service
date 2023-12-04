package org.bahmni.feed.openerp.client;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.worker.WorkerFactory;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FeedClientFactory {


    private final WorkerFactory workerFactory;
    private static Logger logger = LoggerFactory.getLogger(FeedClientFactory.class);

    public FeedClientFactory(WorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
    }

    public AtomFeedClient getFeedClient(OpenERPAtomFeedProperties openERPAtomFeedProperties, AtomFeedSpringTransactionSupport transactionManager, OpenERPContext openERPContext, AllFeeds allFeeds, AllMarkers allMarkers, AllFailedEvents allFailedEvents, Jobs jobName) {
        String feedUri = openERPAtomFeedProperties.getFeedUriForJob(jobName);
        if (StringUtils.isBlank(feedUri)) {
            String message = String.format("No feed-uri defined for Job [%s][%s]", jobName, jobName.getFeedUriRef());
            logger.warn(message);
            throw new FeedException(message);
        }

        try {
            String urlPrefix = getURLPrefix(jobName,openERPAtomFeedProperties);
            EventWorker eventWorker = workerFactory.getWorker(jobName, feedUri, openERPContext, urlPrefix);
            return new AtomFeedClient(allFeeds, allMarkers, allFailedEvents, atomFeedProperties(openERPAtomFeedProperties), transactionManager, new URI(feedUri), eventWorker) ;
        } catch (URISyntaxException e) {
            throw new RuntimeException("error for uri:" + feedUri, e);
        }
    }

    static String getURLPrefix(Jobs jobName, OpenERPAtomFeedProperties atomFeedProperties) {
        String endpointURI = getURIForJob(jobName,atomFeedProperties);
        try {
            if(endpointURI != null && !endpointURI.isEmpty()){
                URL endpointUrl = new URL(endpointURI);
                return String.format("%s://%s", endpointUrl.getProtocol(), endpointUrl.getAuthority());
            } else throw new RuntimeException("Endpoint URI is null or empty");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Is not a valid URI - " + endpointURI, e);
        }
    }

    private static String getURIForJob(Jobs jobName,OpenERPAtomFeedProperties atomFeedProperties){
        switch (jobName){
            case CUSTOMER_FEED:
            case SALEORDER_FEED:
            case SALEABLE_FEED:
            case DRUG_FEED:
            case LAB_FEED:
                return atomFeedProperties.getAuthenticationURI();
            case REFERENCE_DATA_FEED: return atomFeedProperties.getReferenceDataEndpointURI();
            case OPENELIS_SALEORDER_FEED: return atomFeedProperties.getOpenElisURI();
            default: return null;
        }
    }

    static AtomFeedProperties atomFeedProperties(OpenERPAtomFeedProperties openERPAtomFeedProperties) {
        AtomFeedProperties atomFeedProperties = new AtomFeedProperties();
        atomFeedProperties.setControlsEventProcessing(true);
        atomFeedProperties.setConnectTimeout(openERPAtomFeedProperties.getConnectionTimeoutInMilliseconds());
        atomFeedProperties.setReadTimeout(openERPAtomFeedProperties.getReplyTimeoutInMilliseconds());
        atomFeedProperties.setMaxFailedEvents(openERPAtomFeedProperties.getMaxFailedEvents());
        return atomFeedProperties;
    }
}