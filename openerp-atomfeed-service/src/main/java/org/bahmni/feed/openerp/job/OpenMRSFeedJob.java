package org.bahmni.feed.openerp.job;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.client.AtomFeedClientHelper;
import org.ict4h.atomfeed.client.service.AtomFeedClient;

public class OpenMRSFeedJob {
    private static Logger logger = Logger.getLogger(OpenMRSFeedJob.class);

    private AtomFeedClientHelper atomFeedClientHelper;
    private AtomFeedClient atomFeedClient;

    public OpenMRSFeedJob(AtomFeedClientHelper atomFeedClientHelper) throws com.sun.syndication.io.FeedException {
        this.atomFeedClientHelper = atomFeedClientHelper;
    }

    public void processFeed(String feedName, String jobName) {
        try {
            logger.info("Processing " + feedName + ". ");
            initAtomFeedClient(feedName, jobName);
            if(atomFeedClient != null){
                atomFeedClient.processEvents();
            }
        } catch (Exception e) {
            logger.error("failed customer feed execution ", e);
            handleAuthorizationException(e, feedName, jobName);
        }
    }

    private void initAtomFeedClient(String feedName, String jobName) {
        if(atomFeedClient == null){
            atomFeedClient = (AtomFeedClient) atomFeedClientHelper.getAtomFeedClient(feedName, jobName);
        }
    }

    public void processFailedEvents(String feedName, String jobName) {
        try {
            logger.info("Processing failed events for Customer Feed");
            initAtomFeedClient(feedName, jobName);
            if(atomFeedClient != null){
                atomFeedClient.processFailedEvents();
            }
        } catch (Exception e) {
            logger.error("failed customer feed execution ", e);
            handleAuthorizationException(e, feedName, jobName);
        }
    }

    protected void handleAuthorizationException(Throwable e, String feedName, String jobName) throws FeedException {
        if (e != null && ExceptionUtils.getStackTrace(e).contains("HTTP response code: 401")) {
            atomFeedClient = (AtomFeedClient) atomFeedClientHelper.getAtomFeedClient(feedName, jobName);
        }
    }
}